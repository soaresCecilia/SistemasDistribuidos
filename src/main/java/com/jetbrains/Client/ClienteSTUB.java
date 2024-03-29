package com.jetbrains.Client;

import com.jetbrains.*;
import com.jetbrains.Exceptions.*;
import com.jetbrains.Server.Dados.Musica;
import org.apache.log4j.Logger;

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
    private BufferedReader in;
    private InputStream is;
    private OutputStream os;
    public static String PATH_TO_RECEIVE = "/tmp/cliente_soundcloud/";

    private final Logger logger = Logger.getLogger("ClienteStub");

    public ClienteSTUB(String ip, Integer porto){
        this.ip = ip;
        this.porto = porto;
    }

    @Override
    /*
     Método que comunica ao servidor que um utilizador quer fazer login e que verifica se
    essa operação foi ou não bem sucedida.
     */
    public void login(String nome, String password) throws CredenciaisInvalidasException, ClienteServerException {
        // os nomes e password não podem ter espaços
        out.println("login " + nome + " " + password);
        out.flush();

        try {
            String le = in.readLine();

            String[] rsp = le.split(" ");
            switch (rsp[0]) {
                case "1": //correu tudo bem
                    break;
                default:
                    logger.info(le);
                    throw new CredenciaisInvalidasException("Credenciais inválidas");
            }
        }
        catch (IOException e){
            throw new ClienteServerException("Ocorreu um erro na ligação");
        }
    }

    @Override
    /*
    * Metodo que faz logout do cliente no servidor, e termina a ligação do Cliente com o Servidor
    * */
    public void logout(String nome) throws IOException{
        try {
            out.println("logout");
            out.flush();
        }
        finally {
            this.desconectar();
        }
    }

    @Override
    /*
    Método que indica ao servidor que quer registar um utilizador com determinado nome e password e que verifica se
    essa operação foi ou não bem sucedida.
    O nosso protocolo utiliza números inteiros para indicar o estado de uma operação. 1 - Se tudo correu bem e 0 - quando
    a operação não foi concluída.
     */

    public void registarUtilizador(String nome, String password) throws UtilizadorJaExisteException, ClienteServerException {
        out.println("registar " + nome + " " + password);
        out.flush();

        try {
            String le = in.readLine();

                String[] rsp = le.split(" ");
                switch (rsp[0]) {
                    case "1": //correu tudo bem
                        break;
                    default:
                        logger.info(le);
                        throw new UtilizadorJaExisteException("Já existe utilizador com esse login");
                }
        }
        catch (IOException e){
            throw new ClienteServerException("Erro na ligação com o servidor");
        }
    }

    @Override
    /*
    * Metodo que recebe o id da musica a fazer download.
    * Envia uma string, EX: "download idMusica"
    *
    * Obtem a resposta do servidor, se -1 ĺança excessao de erro no Servidor
    * se -2 lança a excessao de que a musica não existe
    *
    * Correndo tudo bem (1), recebe uma resposta do servidor com este formato "1 tamanhoMusica nomeMusica"
    * Armazena a Musica num caminho genérico dado pela seguinte String:  PATH_TO_RECEIVE + nomeMusica + ".mp3"
    * Lê do socket para um array de bytes com tamanho de 1024 bytes, em que de seguida escreve no ficheiro criado
    * no diretorio definido inicialmente
    *
    */
    public void download(int id) throws MusicaInexistenteException, UtilizadorNaoAutenticadoException, ClienteServerException{
        try {

            /*
            Thread.sleep(10000);
            */

            final String idM = "download " + id;

            out.println(idM);
            out.flush();
        }
        catch (NullPointerException e){
            throw new UtilizadorNaoAutenticadoException("Utilizador não se encontra autenticado");
        }

            try {
                String le = in.readLine();

                logger.info(le);

                String[] rsp = le.split(" ");

                switch (rsp[0]) {

                    case "1": //correu tudo bem

                        final int tamanhoFile = Integer.parseInt(rsp[1]);

                        String caminhoGuardarMusica = PATH_TO_RECEIVE + rsp[2] + ".mp3";

                        byte[] mybytearray = new byte[MAX_SIZE];

                        FileOutputStream bos = new FileOutputStream(caminhoGuardarMusica);

                        int bytesIni = 0;
                        int bytesRead = 0;

                        logger.info("Preparar para ler " + tamanhoFile + " bytes");

                        while (bytesIni < tamanhoFile) {

                            bytesRead = Integer.min(tamanhoFile - bytesIni, mybytearray.length);
                            bytesRead = is.read(mybytearray, 0, bytesRead);

                            bos.write(mybytearray, 0, bytesRead);

                            bytesIni += bytesRead;
                        }

                        logger.info("tamanho do ficheiro enviado " + bytesIni);

                        bos.flush();
                        bos.close();

                        break;
                    case "-1":
                        throw new ClienteServerException("Ocorreu erro no Servidor, contacte assistencia técnica.");
                    case "2":
                        throw new MusicaInexistenteException("Não existe esse id nas musicas");

                    default:

                        throw new ClienteServerException("Ocorreu erro no Servidor, contacte assistencia técnica.");
                }
            } catch (IOException e) {
                throw new ClienteServerException("Ocorreu um erro na ligaçao");
            }

    }

    @Override
    /*
    * Metodo que recebe os dados da musica, à qual será feito o upload
    *
    * É determinado o tamanho do ficheiro da musica a transferir, e enviada uma String ao Servidor com o seguinte formato:
    * "upload tamanhoMusica titulo interprete ano genero"
    *
    * Em seguida ocorre aa leitura do ficheiro e sua escrita de 1024 em 1024 bytes para o socket
    *
    */
    public void upload(String caminho, String titulo, String interprete, String ano, String genero) throws UtilizadorNaoAutenticadoException, ClienteServerException{

            try {

                File myFile = new File(caminho);

                int tamanhoFile = (int) myFile.length();

                byte[] mybytearray = new byte[MAX_SIZE];

                FileInputStream bis = new FileInputStream(myFile);

                int bytesIni = 0;
                int bytesRead = 0;
                try {
                    out.println("upload " + tamanhoFile + " " + titulo + " " + interprete + " " + ano + " " + genero);
                    out.flush();
                }
                catch (NullPointerException e){
                    throw new UtilizadorNaoAutenticadoException("Utilizador não se encontra autenticado");
                }

                while (bytesIni < tamanhoFile) {

                    bytesRead = Integer.min(tamanhoFile -bytesIni, mybytearray.length);
                    bytesRead = bis.read(mybytearray, 0, bytesRead);

                    os.write(mybytearray, 0, bytesRead);

                    bytesIni += bytesRead;
                }

                os.flush();
                bis.close();

            } catch (IOException e) {
                throw new ClienteServerException("Ocorreu erro, contacte assistencia técnica.");
            }

            try {
                String le = in.readLine();

                String[] rsp = le.split(" ");
                switch (rsp[0]) {
                    case "1": //correu tudo bem
                        break;
                    default:
                        logger.info(le);
                        throw new ClienteServerException("Ocorreu erro no Servidor, contacte assistencia técnica.");
                }
            } catch (IOException e) {
                throw new ClienteServerException("Ocorreu um erro " + e.getMessage());
            }

    }

    @Override
    /*
    Este método envia para o servidor uma tag de procura mais a etiqueta que pretende e recebe do servidor um inteiro (1 ou 2,
    consoante tenham sido encontradas músicas ou não) e uma String que contém todas as músicas com a referida etiqueta.
    Finalmente, caso tenham sido encontradas músicas devolve uma lista com os metadados das mesmas.
     */
    public List<Musica> procuraMusica (String etiqueta) throws ClienteServerException, MusicaInexistenteException, UtilizadorNaoAutenticadoException {


        try {
            String procuraEtiqueta = "procura " + etiqueta;

            out.println(procuraEtiqueta);
            out.flush();
        } catch (NullPointerException e) {
            throw new UtilizadorNaoAutenticadoException("Utilizador não se encontra autenticado");
        }

        try {
            String le = in.readLine();

            String[] rsp = le.split(" ");

                switch (rsp[0]) {
                    case "1": //correu tudo bem
                        return transformaString(rsp);//manda a lista das musicas
                    case "2":
                        logger.info(le);
                        throw new MusicaInexistenteException("Não existe nenhuma musica com essa etiqueta.");
                    default:
                        logger.info(le);
                        throw new ClienteServerException("Ocorreu erro no Servidor, contacte assistencia técnica.");
                }
            } catch (IOException e) {
                throw new ClienteServerException("Ocorreu o erro " + e.getMessage());
            }

    }


    /*
    Método que percorre um array de strings e transforma-o numa lista de músicas para ser enviada ao Cliente.
     */
    private List<Musica> transformaString(String []s) {
        List<Musica> musicas = new ArrayList<Musica>();
        int id = 0, nDownloads = 0, i = 0;
        String titulo = null, interprete = null, ano = null, genero = null;

        while( i < s.length-1) {
            while (!s[i].equals("-") && i < s.length-1) {
                if (s[i].equals("ID:")) {
                    id = Integer.parseInt(s[i+1]);
                } else if (s[i].equals("Título:")) {
                    titulo = s[i+1];
                } else if (s[i].equals("Interprete:")) {
                   interprete = s[i+1];
                } else if (s[i].equals("Ano:")) {
                    ano = s[i+1];
                } else if (s[i].equals("Genero:")) {
                    genero = s[i+1];
                } else if (s[i].equals("Numero_downloads:")) {
                    nDownloads = Integer.parseInt(s[i + 1]);
                }
                i++;
            }
            Musica m = new Musica(id, titulo, interprete, ano, genero, nDownloads);
            musicas.add(m);

            i++;
        }
        return musicas;
    }

    /*
    Este método cria os elementos necessários para ser estabelecida uma conexão com o Servidor.
     */
    
    public void conectar() throws ClienteServerException {

        try {
            this.socket = new Socket(this.ip, this.porto);
            this.is = socket.getInputStream();
            this.os = socket.getOutputStream();
            this.out = new PrintWriter(this.os);
            this.in = new BufferedReader(new InputStreamReader(this.is));
        }
        catch (IOException e){
            throw new ClienteServerException("Ocorreu erro no Servidor, contacte assistencia técnica");
        }
    }

    /*
    Método que termina a conexão com o Servidor.
     */
    public void desconectar()throws  IOException{
        this.socket.shutdownOutput();
        this.socket.shutdownInput();
        this.socket.close();
    }
}
