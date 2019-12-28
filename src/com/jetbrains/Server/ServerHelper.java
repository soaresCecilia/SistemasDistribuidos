package com.jetbrains.Server;

import com.jetbrains.*;
import com.jetbrains.Exceptions.*;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerHelper implements SoundCloud {
    private Repositorio repositorio;
    private Socket clSock;
    public static String PATH_TO_RECORD = "/home/luisabreu/Desktop/musicaS/";
    private PrintWriter out;
    private BufferedReader in;

    public ServerHelper(Socket clsock, Repositorio r) throws IOException {
        this.repositorio = r;
        this.clSock = clsock;
        this.out = new PrintWriter(clSock.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(clSock.getInputStream()));
    }

    @Override
    /*
    A função login verifica se o utilizador inseriu os dados correctos e se o mesmo deve ser autenticado.
     */
    public void login(String nome, String password) throws IOException, CredenciaisInvalidasException, ClientesSTUBException {
            Utilizador utilizador;

            utilizador = repositorio.getUtilizadores().get(nome);

            if (utilizador == null || !utilizador.autentica(nome, password)) {
                throw new CredenciaisInvalidasException("Utilizador não está registado");
            }
            else if (!utilizador.getActivo() && utilizador.autentica(nome, password)) {
                utilizador.setActivo();
            }
    }

    @Override
    /*
    A função logout altera o estado do utilizador para inactivo.
     */
    public void logout(String nome) throws IOException, ClientesSTUBException {
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
    public void registarUtilizador(String nome, String password) throws UtilizadorJaExisteException, ClientesSTUBException, CredenciaisInvalidasException{
        if (!repositorio.getUtilizadores().containsKey(nome)) {
            if(!nome.contains(" ") && !password.contains(" ")) {
                Utilizador novoUtilizador = new Utilizador(nome, password);
                repositorio.getUtilizadores().put(nome, novoUtilizador);
                out.println("1");
                out.flush();
            }
            else {
                throw new CredenciaisInvalidasException("O nome ou a password não podem conter espaços");
            }
        }
        else {
            throw new UtilizadorJaExisteException("Já se encontra registado um utilizador com esse mesmo nome.");
        }

    }

    @Override
    public void download(int idMusica) throws IOException, MusicaInexistenteException, UtilizadorNaoAutenticadoException, ClientesSTUBException {
        boolean n = repositorio.getMusicas().containsKey(idMusica);

        System.out.println("Existe ou não: "+n);

        if(repositorio.getMusicas().containsKey(idMusica)){

            Musica m = repositorio.getMusicaId(idMusica);

            File myFile = new File(m.getCaminhoFicheiro());

            byte[] mybytearray = new byte[(int) myFile.length()];

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));

            bis.read(mybytearray, 0, mybytearray.length);

            OutputStream os = clSock.getOutputStream();

            //envia a string "1 tamanhaFicheiroAler"
            System.out.println("tamanho do ficheiro lido: "+ mybytearray.length);

            String okTam = "1 "+mybytearray.length+" "+m.getTitulo();
            System.out.println(okTam);
            out.println(okTam);
            out.flush();
            System.out.println("fez bem o flucsh");
            //envia ficheiro pedido
            os.write(mybytearray, 0, mybytearray.length);

            os.flush();
        }
        else{
            throw new MusicaInexistenteException();

        }


    }

    @Override
    public void upload(String tamanho, String titulo, String interprete,  String ano, String genero) throws IOException, UtilizadorNaoAutenticadoException, ClientesSTUBException {

        String caminho = PATH_TO_RECORD+titulo+".mp3";

        int tamanhoFile =  Integer.parseInt(tamanho);

        byte[] mybytearray = new byte[1024];

        InputStream is = clSock.getInputStream();

        FileOutputStream fos = new FileOutputStream(caminho);

        BufferedOutputStream bos = new BufferedOutputStream(fos);

        int bytesIni=0;

        while (bytesIni<tamanhoFile)       {
            int bytesRead = is.read(mybytearray, 0, mybytearray.length);
            bos.write(mybytearray, 0, bytesRead);

            bytesIni+= bytesRead;
        }

        bos.close();
        bos.flush();

        Musica m= new Musica(titulo,interprete,ano,genero, caminho);
        repositorio.addMusica(m);

        out.println("1");
        out.flush();
    }

    @Override
    /*
    A função procura uma música devolve uma lista vazias e envia para o ClienteStub
    uma String com todas as músicas que tenham em algum dos seus metadados a String passada como parâmetro.
     */
    public List<Musica> procuraMusica(String etiqueta) throws IOException, UtilizadorNaoAutenticadoException, MusicaInexistenteException, ClientesSTUBException {
        List<String> musicasComEtiqueta = new ArrayList<>();
        List<Musica> vazia = new ArrayList<Musica>(); //lista vazia que vai ser devolvida apenas para obedecer ao interface
        this.out = new PrintWriter(clSock.getOutputStream());

        for(Musica m : repositorio.getMusicas().values()) {
            if (m.procuraEtiqueta(etiqueta)) {
                musicasComEtiqueta.add(m.toString());
            }
        }

       // System.out.println("O tamanho da lista da etiqueta é " + musicasComEtiqueta.size());

        //sinal delimitador de que começa uma nova música
        String delim = " - ";
        String res = String.join(delim, musicasComEtiqueta);
        String resultado = "1 " + res;


        System.out.println(res);

        System.out.println(resultado);

        if (musicasComEtiqueta.size() == 0) {
            throw new MusicaInexistenteException("Não existem músicas com essa etiqueta");
        }

        out.println(resultado);
        out.flush();


        return vazia;
    }


}
