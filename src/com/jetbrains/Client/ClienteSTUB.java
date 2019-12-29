package com.jetbrains.Client;

import com.jetbrains.*;
import com.jetbrains.Exceptions.*;
import com.jetbrains.Server.Musica;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClienteSTUB implements SoundCloud {
    public static final int MAX_SIZE = 1024;
    private Socket socket;
    private final String ip;
    private final Integer porto;
    private PrintWriter out;
    private BufferedReader inBuffer;

    private boolean activo;

    public static String PATH_TO_RECEIVE = "/home/luisabreu/Desktop/musicaC/";
    //public static String PATH_TO_RECEIVE = "/home/luisabreu/Desktop/musicaC/";
    //public static String PATH_TO_RECEIVE = "/home/luisabreu/Desktop/musicaC/";
    //public static String PATH_TO_RECEIVE = "/home/luisabreu/Desktop/musicaC/";

    public ClienteSTUB(String ip, Integer porto){
        this.ip = ip;
        this.porto = porto;
        this.activo = false;
    }


    @Override
    /*
     Método que comunica ao servidor que um utilizador quer fazer login e que verifica se
    essa operação foi ou não bem sucedida.
     */
    public void login(String nome, String password) throws CredenciaisInvalidasException, ClientesSTUBException {
        // os nomes e password não podem ter espaços
        out.println("login " + nome + " " + password);
        out.flush();

        try {
            String le = inBuffer.readLine();

            String[] rsp = le.split(" ");
            switch (rsp[0]) {
                case "1": //correu tudo bem

                    this.activo = true;

                    break;
                default:
                    throw new CredenciaisInvalidasException("Credenciais inválidas");
            }
        }
        catch (IOException e){
            throw new ClientesSTUBException("Ocorreu um erro na ligação");
        }
    }

    @Override
    public void logout(String nome) throws IOException{
        try {
            out.println("logout");
            out.flush();
        }
        finally {
            this.desconectar();
            this.activo = false;
        }
    }

    @Override
    /*
    Método que indica ao servidor que quer registar um utilizador com determinado nome e password e que verifica se
    essa operação foi ou não bem sucedida.
    O nosso protocolo utiliza números inteiros para indicar o estado de uma operação. 1 - Se tudo correu bem e 0 - quando
    a operação não foi concluída.
     */
    public void registarUtilizador(String nome, String password) throws UtilizadorJaExisteException, ClientesSTUBException, CredenciaisInvalidasException{

        out.println("registar " + nome + " " + password);
        out.flush();

        try {
            String le = inBuffer.readLine();

                String[] rsp = le.split(" ");
                switch (rsp[0]) {
                    case "1": //correu tudo bem
                        break;
                    case "0":
                        throw new UtilizadorJaExisteException("Já existe utilizador com esse login");
                    default:
                        throw new CredenciaisInvalidasException("Já se encontra registado um utilizador com esse mesmo nome.");
                }
        }
        catch (IOException e){
            throw new ClientesSTUBException("Erro na ligação com o servidor");
        }
    }

    @Override
    public void download(int id) throws MusicaInexistenteException, UtilizadorNaoAutenticadoException, ClientesSTUBException{

        if( this.activo ) {
            final String idM = "download " + id;

            out.println(idM);
            out.flush();

            System.out.println("do que eu fiz flush: " + idM);

            try {

                String le = inBuffer.readLine();

                String[] rsp = le.split(" ");
                switch (rsp[0]) {

                    case "1": //correu tudo bem

                        final int tamanhoFile = Integer.parseInt(rsp[1]);

                        String caminhoGuardarMusica = PATH_TO_RECEIVE + rsp[2] + ".mp3";

                        byte[] mybytearray = new byte[MAX_SIZE];

                        InputStream is = socket.getInputStream();

                        FileOutputStream fos = new FileOutputStream(caminhoGuardarMusica);

                        BufferedOutputStream bos = new BufferedOutputStream(fos);

                        int bytesIni = 0;
                        while (bytesIni < tamanhoFile) {

                            int bytesRead = is.read(mybytearray, 0, mybytearray.length);

                            bos.write(mybytearray, 0, bytesRead);

                            bytesIni += bytesRead;
                        }

                        bos.close();

                        break;
                    default:
                        throw new MusicaInexistenteException("Não existe esse id nas musicas");
                }
            } catch (IOException e) {
                throw new ClientesSTUBException("Ocorreu um erro na ligaçao");
            }
        }
        else {
                throw new UtilizadorNaoAutenticadoException("Utilizador não autenticado.");
        }
    }

    @Override
    public void upload(String caminho, String titulo, String interprete, String ano, String genero) throws UtilizadorNaoAutenticadoException, ClientesSTUBException{

        if( this.activo ) {
            try {

                File myFile = new File(caminho);

                int tamFile = (int) myFile.length();

                byte[] mybytearray = new byte[MAX_SIZE];  //mudar isso para fazer upload de MAX_SIZE de cada vez

                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));

                OutputStream os = socket.getOutputStream();

                int bytesIni = 0;
                int bytesRead;

                out.println("upload " + tamFile + " " + titulo + " " + interprete + " " + ano + " " + genero);
                out.flush();

                while (bytesIni < tamFile) {
                    bytesRead = bis.read(mybytearray, 0, mybytearray.length);
                    os.write(mybytearray, 0, mybytearray.length);
                    os.flush();
                    bytesIni += bytesRead;
                }
            } catch (IOException e) {
            }
            ;

            try {
                String le = inBuffer.readLine();

                String[] rsp = le.split(" ");
                switch (rsp[0]) {
                    case "1": //correu tudo bem
                        break;
                    default:
                        throw new ClientesSTUBException("Erro indefinido");
                }
            } catch (IOException e) {
                throw new ClientesSTUBException("Ocorreu um erro com o servidor");
            }
        }
        else {
            throw new UtilizadorNaoAutenticadoException("Utilizador não autenticado.");
        }
    }

    @Override
    /*
    Este método envia para o servidor uma tag de procura mais a etiqueta que pretende e recebe do servidor um inteiro (1 ou 2,
    consoante tenham sido encontradas músicas ou não) e uma String que contém todas as músicas com a referida etiqueta.
    Finalmente, caso tenham sido encontradas músicas devolve uma lista com os metadados das mesmas.
     */
    public List<Musica> procuraMusica (String etiqueta) throws ClientesSTUBException, UtilizadorNaoAutenticadoException, MusicaInexistenteException{
        if(this.activo) {
            String procuraEtiqueta = "procura " + etiqueta;

            out.println(procuraEtiqueta);
            out.flush();

            try {
                String le = inBuffer.readLine();

                String[] rsp = le.split(" ");

                switch (rsp[0]) {
                    case "1": //correu tudo bem
                        return transformaString(rsp);//manda a lista das musicas
                    case "2":
                        throw new MusicaInexistenteException("Não existe musica com essa etiqueta.");
                    default:
                        throw new ClientesSTUBException("Erro indefinido");
                }
            } catch (IOException e) {
                throw new ClientesSTUBException("Ocorreu um erro com o servidor");
            }
        }
        else {
            throw new UtilizadorNaoAutenticadoException("Utilizador não autenticado.");
        }
    }


    /*
    Método que percorre um array de strings e transforma-o numa lista de músicas para ser enviada ao Cliente.
     */
    private List<Musica> transformaString(String []s) {
        List<Musica> musicas = new ArrayList<Musica>();
        int id = 0, nDownloads = 0, i = 0;
        String titulo= null, interprete = null, ano = null, genero = null, caminhoFicheiro = null;

        while( i < s.length-1) {
            while (!s[i].equals("-") && i < s.length-1) {
                if (s[i].equals("ID:")) {
                    id = Integer.parseInt(s[i + 1]);
                } else if (s[i].equals("Título:")) {
                    titulo = s[i + 1];
                } else if (s[i].equals("Interprete:")) {
                    interprete = s[i + 1];
                } else if (s[i].equals("Ano:")) {
                    ano = s[i + 1];
                } else if (s[i].equals("Genero:")) {
                    genero = s[i + 1];
                } else if (s[i].equals("Caminho:")) {
                    caminhoFicheiro = s[i + 1];
                } else if (s[i].equals("Numero_downloads:")) {
                    nDownloads = Integer.parseInt(s[i + 1]);
                }
                i++;
            }
            Musica m = new Musica(id, titulo, interprete, ano, genero, caminhoFicheiro, nDownloads);
            musicas.add(m);
            i++;
        }
        return musicas;
    }


    /*
    Este método cria os elementos necessários para ser estabelecida uma conexão.
     */
    public void conectar() throws ClientesSTUBException {
        try {
            this.socket = new Socket(this.ip,this.porto);
            out = new PrintWriter(socket.getOutputStream());
            inBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e){
            throw new ClientesSTUBException("Ocorreu um erro na ligação com o servidor");
        }
    }

    /*
    Método que termina a conexão.
     */
    private void desconectar()throws  IOException{
        this.socket.shutdownOutput();
        this.socket.shutdownInput();
        this.socket.close();
    }


}
