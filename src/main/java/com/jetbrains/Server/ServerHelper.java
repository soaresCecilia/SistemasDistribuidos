package com.jetbrains.Server;

import com.jetbrains.*;
import com.jetbrains.Server.Dados.Musica;
import com.jetbrains.Server.Dados.Repositorio;
import com.jetbrains.Server.Dados.Utilizador;
import org.apache.log4j.Logger;

import java.io.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerHelper implements SoundCloud {
    private Repositorio repositorio;
    private Socket socket;
    public static String PATH_TO_RECORD = "/tmp/servidor_soundcloud/";
    private PrintWriter out;
    private BufferedReader in;
    private OutputStream os;
    private InputStream is;
    public static final int MAX_SIZE = 1024; //tamanho de transferência de ficheiros limitado
    private final Logger logger = Logger.getLogger("ServerHelper");

    public ServerHelper(Socket clienteSock, Repositorio r) throws IOException {
        this.repositorio = r;
        this.socket = clienteSock;
        this.os = socket.getOutputStream();
        this.is = socket.getInputStream();
        this.out = new PrintWriter(this.os);
        this.in = new BufferedReader(new InputStreamReader(this.is));
    }

    @Override
    /*
    A função login verifica se o utilizador inseriu os dados correctos e se o mesmo deve ser autenticado.
     */
    public void login(String nome, String password) {
            Utilizador utilizador;

            utilizador = repositorio.getUtilizadores().get(nome);

            if (utilizador == null || !utilizador.autentica(nome, password)) {
                out.println("0");
                out.flush();
            }
            else if (utilizador.autentica(nome, password)) { //basta que a autenticação esteja correcta não importa se faz login já estando loggado
                utilizador.setActivo();
                out.println("1");
                out.flush();
            }
    }

    @Override
    /*
    A função logout altera o estado do utilizador para inactivo.
     */
    public void logout(String nome) {
        Utilizador utilizador = repositorio.getUtilizadores().get(nome);

        if (utilizador != null && utilizador.getActivo()) {
            utilizador.setInactivo();
        }
    }

    @Override
    /*
    Esta função adiciona o utilizador ao hashmap de utilizadores que o servidor dispõe como utilizadores correctamente
    registados. De notar que não podem existir dois utilizadores registados com o mesmo nome, nem o nome ou a password
    podem conter quaquer espaço, senão o nosso portocolo de transfeência de informação entre cliente e servidor não funciona.
     */
    public synchronized void registarUtilizador(String nome, String password) {

        if (!repositorio.getUtilizadores().containsKey(nome)) {
                Utilizador novoUtilizador = new Utilizador(nome, password);
                repositorio.getUtilizadores().put(nome, novoUtilizador);
                novoUtilizador.setActivo();
                out.println("1");
                out.flush();
        }
        else {
            //utilizador já existe
            out.println("0");
            out.flush();
        }
    }

    @Override
    public void download(int idMusica) {

        boolean existe = repositorio.existeMusica(idMusica);

        try {
            if (existe) {

                Musica m = repositorio.getMusicaId(idMusica);

                File myFile = new File(m.getCaminhoFicheiro());

                int tamanhoFile = (int) myFile.length();

                byte[] mybytearray = new byte[MAX_SIZE];

                FileInputStream bis = new FileInputStream(myFile);

                //envia a string "1 tamanhaFicheiroAler nomeFicheiro"
                String okTam = "1 " + tamanhoFile + " " + m.getTitulo();

                out.println(okTam);
                out.flush();

                /*
                try {
                    Thread.sleep(30000);
                }catch (InterruptedException e) { }
                */
                //envia ficheiro pedido

                int bytesIni = 0;
                int bytesRead;

                logger.info("Tamanho do ficheiro " + tamanhoFile);

                while (bytesIni < tamanhoFile) {

                    bytesRead = Integer.min(tamanhoFile - bytesIni, mybytearray.length);
                    bytesRead = bis.read(mybytearray, 0, bytesRead);

                    os.write(mybytearray, 0, bytesRead);

                    bytesIni += bytesRead;
                }

                bis.close();
                os.flush();

                logger.info("Acabei o download da musica: "+idMusica);
                logger.info("Tamanho do ficheiro " + bytesIni);

                //aumenta o numero de downloads da musica
                repositorio.growNDownloads(idMusica);
            } else {
                out.println("2");
                out.flush();
            }
        }catch (IOException e) {
            out.println("0");
            out.flush();
        }
    }

    @Override

    public void upload(String tamanho, String titulo, String interprete,  String ano, String genero) {

        try {
            String caminho = PATH_TO_RECORD + titulo + ".mp3";

            int tamanhoFile = Integer.parseInt(tamanho);

            byte[] mybytearray = new byte[MAX_SIZE];

            FileOutputStream bos = new FileOutputStream(caminho);

            int bytesIni = 0;
            int bytesRead = 0;

            while (bytesIni < tamanhoFile) {

                bytesRead = Integer.min(tamanhoFile - bytesIni, mybytearray.length);
                bytesRead = is.read(mybytearray, 0, bytesRead);

                bos.write(mybytearray, 0, bytesRead);
                bos.flush();

                bytesIni += bytesRead;
            }

            bos.flush();
            bos.close();

            Musica m = new Musica(0, titulo, interprete, ano, genero, caminho, 0);

            repositorio.addMusica(m);

            //correu tudo bem
            out.println("1");
            out.flush();
            logger.info("Acabei o upload da musica: "+titulo);
            logger.info("Tamanho do ficheiro: "+tamanhoFile);
        }catch (IOException e) {
            out.println("0");
            out.flush();
        }
    }



    @Override
    /*
    A função procura uma música devolve uma lista vazias e envia para o ClienteStub
    uma String com todas as músicas que tenham em algum dos seus metadados a String passada como parâmetro.
     */
    public List<Musica> procuraMusica(String etiqueta) {
        List<String> musicasComEtiqueta = new ArrayList<>();
        List<Musica> vazia = new ArrayList<Musica>(); //lista vazia que vai ser devolvida apenas para obedecer ao interface

        for (Musica m : repositorio.getMusicas().values()) {
            if (m.procuraEtiqueta(etiqueta)) {
                musicasComEtiqueta.add(m.toString());
            }
        }

        logger.info("O tamanho da lista da etiqueta é " + musicasComEtiqueta.size());

        //sinal delimitador de que começa uma nova música
        final CharSequence delim = " - ";
        String res = String.join(delim, musicasComEtiqueta);
        String resultado = "1 " + res;

        logger.info("As musicas com a etiqueta são " + res);
        logger.info("As musicas com a etiqueta e com o indicador de que correu tudo bem - 1 " + resultado);

        if (musicasComEtiqueta.size() == 0) {
            out.println("2");
            out.flush();
            return vazia;
        }

        out.println(resultado);
        out.flush();

        return vazia;
    }
}
