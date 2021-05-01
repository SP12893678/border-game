import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class MyServer {

     Console console;
     Fontloader font;
     Vector<Integer> gobang_roomEmpty;
     Vector<Integer> gobang_roomWait;
     Map<String, PrintStream> login_user_stream;
     Map<String, String> platform_user_ip;
     Map<String, PrintStream> platform_user_stream;
     Map<String, String> platform_user_status;
     /*
      * value n|m n -> 0 -> no start game 1 -> start game m -> game name
      */
     Map<String, String> gobang_user_ip;
     Map<String, PrintStream> gobang_user_stream;
     Map<String, String> gobang_user_status;
     /*
      * value n|m n -> 0 -> hall 1 -> waitroom not start game 2 -> game start already
      * m -> room's number
      */
     Map<Integer, ArrayList<Object>> gobang_room;
     /*
      * key>>roomNumber value,1>>Number of people value,2>>roomTitle
      * value,3>>Player1(name) value,4>>Player2(name)
      */
     ArrayList<Object> gobang_room_list;
     ServerSocket serverSock;
     int count = 0;
     int login_count = 0;
     int user_count = 0;
     int gobang_count = 0;

     Vector<Room> room_vector;

     public class Room {
          private int[][] board;
          private String player1;
          private boolean readyPlayer1;
          private String player2;
          private boolean readyPlayer2;
          public int count;

          public Room(String player1, String player2) {
               this.player1 = player1;
               this.player2 = player2;
               this.readyPlayer1 = true;
               this.readyPlayer2 = true;
               board = new int[15][15];
               count = 1;
          }

          public void setBoard(int[][] board) {
               this.board = board;
          }

          public int[][] getBoard() {
               return board;
          }

          public void setPlayer1(String player1) {
               this.player1 = player1;
          }

          public String getPlayer1() {
               return player1;
          }

          private void setReady1(boolean flag) {
               this.readyPlayer1 = flag;
          }

          private boolean getReady1() {
               return readyPlayer1;
          }

          public void setPlayer2(String player2) {
               this.player2 = player2;
          }

          public String getPlayer2() {
               return player2;
          }

          private void setReady2(boolean flag) {
               this.readyPlayer2 = flag;
          }

          private boolean getReady2() {
               return readyPlayer2;
          }
     }

     public static void main(String args[]) {
          new MyServer().go();
     }

     public void go() {
          // 建立物件陣列
          count = 0;
          font = new Fontloader();
          console = new Console();
          login_user_stream = new HashMap<>();
          platform_user_stream = new HashMap<>();
          platform_user_ip = new HashMap<>();
          platform_user_status = new HashMap<>();
          gobang_user_stream = new HashMap<>();
          gobang_user_ip = new HashMap<>();
          gobang_user_status = new HashMap<>();
          gobang_room = new HashMap<>();
          gobang_roomEmpty = new Vector<Integer>();
          gobang_roomWait = new Vector<Integer>();

          room_vector = new Vector<Room>();
          try {
               int port = 8888;
               serverSock = new ServerSocket(port);
               /*-----------------------------------------------------*/
               long yourmilliseconds = System.currentTimeMillis();
               SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY HH:mm");
               Date resultdate = new Date(yourmilliseconds);
               String time = sdf.format(resultdate);
               console.setServerStartTime(time);
               /*-----------------------------------------------------*/
               final DatagramSocket socket = new DatagramSocket();
               socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
               String ip = socket.getLocalAddress().getHostAddress();
               console.setIP(ip);
               /*-----------------------------------------------------*/
               console.showPlatform();
               while (true) {
                    Socket cSocket = serverSock.accept();
                    count++;
                    Thread t = new Thread(new Process(cSocket));
                    t.start();
               }
          } catch (Exception ex) {
               System.out.println("連接失敗");
          }
     }

     public class Process implements Runnable {
          BufferedReader reader;
          Socket sock;

          public Process(Socket cSocket) {
               try {
                    sock = cSocket;
                    InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                    reader = new BufferedReader(isReader);
               } catch (Exception ex) {
                    System.out.println("連接失敗Process1");
               }
          }

          public void run() {
               String action;
               String message;
               String username = null;
               PrintStream writer;
               try {
                    while ((message = reader.readLine()) != null) {
                         System.out.println(message);
                         // console.logPrint(message);
                         action = message.substring(0, message.indexOf(" "));
                         message = message.substring(message.indexOf(" ") + 1);
                         username = gobang_user_ip.get(sock.getRemoteSocketAddress().toString());
                         switch (action) {
                         case "Login_online":
                              writer = new PrintStream(sock.getOutputStream());
                              login_user_stream.put(sock.getRemoteSocketAddress().toString(), writer);
                              console.logPrint("IP → [" + sock.getRemoteSocketAddress().toString() + "] Connect!");
                              login_count++;
                              console.setLoginPageCount(login_count);
                              break;
                         case "Login":
                              login(message);
                              break;
                         case "User_online":
                              username = message;
                              platform_user_ip.put(sock.getRemoteSocketAddress().toString(), username);
                              writer = new PrintStream(sock.getOutputStream());
                              platform_user_stream.put(username, writer);
                              platform_user_status.put(username, "0");
                              console.logPrint("User → [" + username + "] Login!");
                              user_count++;
                              console.setUserOnlineCount(user_count);
                              break;
                         case "Sign_up":
                              sign_up(message);
                              break;
                         case "Gobang_online":
                              username = message;
                              gobang_user_ip.put(sock.getRemoteSocketAddress().toString(), username);
                              writer = new PrintStream(sock.getOutputStream());
                              gobang_user_stream.put(username, writer);
                              gobang_user_status.put(username, "0");
                              platform_user_status.put(username, "1|Gobang");
                              console.logPrint("User → [" + username + "] Online Gobang!");
                              gobang_count++;
                              console.setGobangOnlineCount(gobang_count);
                              break;
                         case "Gobang_Creat_Room":
                              creatRoom(message, username);
                              break;
                         case "Gobang_Room_List":
                              waitroom_list(username);
                              break;
                         case "Gobang_Join_Room":
                              if (Integer.valueOf(message) <= gobang_room.size() && Integer.valueOf(message) > 0) {
                                   joinRoom(Integer.valueOf(message), username);
                              } else {
                                   writer = gobang_user_stream.get(username);
                                   writer.println("Join_Room " + "error " + "此房無人");
                                   writer.flush();
                              }
                              break;
                         case "Gobang_Exit_Room":
                              exitRoom(Integer.valueOf(message), username);
                              break;
                         case "Gobang_Random_Match":
                              randomMatch(message);
                              break;
                         case "Gobang_online_Start":
                              test(message);
                              break;
                         case "Point":
                              addPiece(message);
                              break;
                         case "Ready":
                              ready(message);
                         default:
                              break;
                         }
                    }
                    disconnect(username);
               } catch (Exception ex) {
                    disconnect(username);
               }
          }

          public void disconnect(String username) {
               Object in_login = login_user_stream.get(sock.getRemoteSocketAddress().toString());
               Object in_platform = platform_user_stream.get(username);
               Object in_gobang = gobang_user_stream.get(username);
               if (in_login != null) {
                    console.logPrint("IP → [" + sock.getRemoteSocketAddress().toString() + "] Disconnect!");
                    login_user_stream.remove(sock.getRemoteSocketAddress().toString());
                    login_count--;
                    console.setLoginPageCount(login_count);
               } else if (in_platform != null) {
                    if (platform_user_status.get(username).toString().substring(0, 1).equals("0")) {
                         platform_user_stream.remove(username);
                         platform_user_ip.remove(sock.getRemoteSocketAddress().toString());
                         platform_user_status.remove(username);
                         console.logPrint("User → [" + username + "] Offline!");
                         user_count--;
                         console.setUserOnlineCount(user_count);
                    } else {
                         platform_user_status.put(username, "0");
                         if (in_gobang != null) {
                              String user_status = gobang_user_status.get(username).substring(0, 1);
                              if (user_status.equals("1")) // waitroom
                              {
                                   int Num = Integer.valueOf(gobang_user_status.get(username).substring(2));
                                   exitRoom(Num, username);
                              } else if (user_status.equals("2")) // game start already
                              {
                                   // something that you need to deal with your opponent of gmae
                              }
                              console.logPrint("User → [" + username + "] Offline Gobang!");
                              gobang_user_stream.remove(username);
                              gobang_user_ip.remove(sock.getRemoteSocketAddress().toString());
                              gobang_user_status.remove(username);
                              gobang_count--;
                              console.setGobangOnlineCount(gobang_count);
                         }
                    }
               }
          }

          public void login(String message) {
               String send_message = "empty";
               String username = message.substring(0, message.indexOf("|"));
               String password = message.substring(message.indexOf("|") + 1);
               try {
                    FileReader file = new FileReader("./res/userdata/userData.txt");
                    BufferedReader reader = new BufferedReader(file);
                    String uid;
                    while ((uid = reader.readLine()) != null) {
                         String data_username = uid.substring(0, uid.indexOf("|"));
                         String data_password = uid.substring(uid.indexOf("|") + 1);
                         if (data_username.equals(username)) {
                              if (data_password.equals(password)) {
                                   Object in_platform = platform_user_stream.get(username);
                                   if (in_platform != null) {
                                        send_message = "login already exists";
                                   } else {
                                        send_message = "success";
                                   }
                                   break;
                              } else {
                                   send_message = "wrong password";
                                   break;
                              }
                         } else
                              send_message = "username not found";
                    }
                    reader.close();
                    /*------------------------------------------------------------*/
                    PrintStream writer = login_user_stream.get(sock.getRemoteSocketAddress().toString());
                    writer.println("Login " + send_message);
                    writer.flush();
               } catch (Exception ex) {
                    System.out.println("連接失敗Process2");
               }
          }

          public void sign_up(String message) {
               String send_message = "empty";
               String username = message.substring(0, message.indexOf("|"));
               try {
                    FileReader file = new FileReader("./res/userdata/userData.txt");
                    BufferedReader reader = new BufferedReader(file);
                    String uid;
                    while ((uid = reader.readLine()) != null) {
                         String data_username = uid.substring(0, uid.indexOf("|"));
                         if (data_username.equals(username)) {
                              send_message = "username is exists";
                              break;
                         } else {
                              send_message = "username not found";
                         }
                    }
                    reader.close();
               } catch (Exception ex) {
                    System.out.println("userData.txt檔案不存在");
               }

               if (send_message.equals("username not found")) {
                    PrintWriter outputStream = null;
                    try {
                         outputStream = new PrintWriter(new FileOutputStream("./res/userdata/userData.txt", true));
                         console.logPrint("User → [" + username + "] Sign up!");
                    } catch (FileNotFoundException e) {
                         System.exit(0);
                    }
                    outputStream.append(message + "\r\n");
                    outputStream.close();
               }
               try {
                    /*------------------------------------------------------------*/
                    PrintStream writer = (PrintStream) login_user_stream.get(sock.getRemoteSocketAddress().toString());
                    writer.println("Sign_up " + send_message);
                    writer.flush();
               } catch (Exception e) {
                    System.out.println("連接失敗Process3");
               }
          }

          public void creatRoom(String room_title, String username) {
               int Num;
               if (gobang_roomEmpty.size() == 0) {
                    gobang_room_list = new ArrayList<Object>();
                    gobang_room_list.add(1); // num of ppl
                    gobang_room_list.add(room_title); // title
                    gobang_room_list.add(username); // player1
                    gobang_room.put(gobang_room.size() + 1, gobang_room_list);
                    Num = gobang_room.size();
                    gobang_roomWait.add(Num);
                    gobang_user_status.put(username, "1|" + String.valueOf(Num));
               } else {
                    Num = (int) (gobang_roomEmpty.get(0));
                    gobang_room_list = gobang_room.get(Num);
                    gobang_room_list.set(0, 1);
                    gobang_room_list.set(1, room_title);
                    gobang_room_list.set(2, username);
                    gobang_room.put(Num, gobang_room_list);
                    gobang_roomEmpty.remove(0);
                    gobang_roomWait.add(Num);
                    gobang_user_status.put(username, "1|" + String.valueOf(Num));
               }
               /*------------------------------------------------------------*/
               PrintStream writer = gobang_user_stream.get(username);
               writer.println("Creat_Room " + Num + "|" + room_title + "|" + username);
               writer.flush();
          }

          public void joinRoom(int Num, String username) {
               gobang_roomWait.remove((Integer) Num);
               gobang_room_list = gobang_room.get(Num);
               gobang_room_list.set(0, 2); // num of ppl
               if (gobang_room_list.size() < 4)
                    gobang_room_list.add(username); // player2
               else
                    gobang_room_list.set(3, username);
               gobang_room.put(Num, gobang_room_list);
               gobang_user_status.put(username, "1|" + String.valueOf(Num));

               String title = gobang_room_list.get(1).toString();
               String player1 = gobang_room_list.get(2).toString();
               String player2 = gobang_room_list.get(3).toString();
               /*------------------------------------------------------------*/
               PrintStream writer = gobang_user_stream.get(player2);
               writer.println("Join_Room " + Num + "|" + title + "|" + player2 + "|" + player1);
               writer.flush();
               /*------------------------------------------------------------*/
               writer = gobang_user_stream.get(player1);
               writer.println("Refresh_Room " + Num + "|" + title + "|" + player1 + "|" + player2);
               writer.flush();
          }

          public void exitRoom(int Num, String username) {
               gobang_room_list = gobang_room.get(Num);
               int number = (int) gobang_room_list.get(0);
               gobang_room_list.set(0, number - 1);
               if (number == 2) {
                    gobang_roomWait.add(Num);

                    /*------------------------------------------------------------*/
                    if (gobang_room_list.get(2).toString().equals(username)) {
                         username = gobang_room_list.get(3).toString();
                         gobang_room_list.set(2, username);
                    } else {
                         username = gobang_room_list.get(2).toString();
                    }

                    PrintStream writer = gobang_user_stream.get(username);
                    writer.println("Exit_Room " + username);
                    writer.flush();
               } else {
                    gobang_roomEmpty.add(Num);
                    gobang_roomWait.remove((Integer) Num);
               }
               gobang_room.put(Num, gobang_room_list);
               gobang_user_status.put(username, "0");
          }

          public void randomMatch(String username) {
               PrintStream writer = gobang_user_stream.get(username);
               if (gobang_roomWait.size() == 0) {
                    writer.println("Error " + "無人開房");
                    writer.flush();
               } else {
                    int number = gobang_roomWait.size();
                    int Num = (int) gobang_roomWait.get((int) (Math.random()) % number);
                    joinRoom(Num, username);
               }
          }

          public void waitroom_list(String username) {
               PrintStream writer = gobang_user_stream.get(username);
               writer.println("New_List new");
               Collections.sort(gobang_roomWait);
               gobang_roomWait.forEach(num -> {
                    gobang_room_list = gobang_room.get(num);
                    String title = gobang_room_list.get(1).toString();
                    String roomMaster = gobang_room_list.get(2).toString();
                    writer.println("Join_List " + num + "|" + title + "|" + roomMaster);
                    writer.flush();
               });
               writer.println("Show_List show");
          }

          public void test(String message) {
               String player1 = message.substring(0, message.indexOf("|"));
               String player2 = message.substring(message.indexOf("|") + 1);

               boolean flag = true;

               Room room = new Room(player1, player2);

               for (Room r : room_vector) {
                    if (r.getPlayer1().equals(player1) && r.getPlayer2().equals(player2)) {
                         flag = false;
                         room = r;
                    }
               }

               if (flag) {
                    room_vector.add(room);
               }

               if (room.getReady1() && room.getReady2()) {
                    PrintStream writer = gobang_user_stream.get(player1);
                    writer.println("test test");
                    writer.flush();
                    writer = gobang_user_stream.get(player2);
                    writer.println("test test");
                    writer.flush();
               } else {
                    PrintStream writer = gobang_user_stream.get(player1);
                    writer.println("Error player2 not ready");
                    writer.flush();
               }
          }

          public void addPiece(String message) {
               String player = message.substring(0, message.indexOf("|"));
               message = message.substring(message.indexOf("|") + 1);
               int x = Integer.valueOf(message.substring(0, message.indexOf("|")));
               int y = Integer.valueOf(message.substring(message.indexOf("|") + 1));

               try {
                    Room room = null;
                    int i = 0;
                    int[][] board = new int[15][15];
                    for (Room r : room_vector) {
                         if (r.getPlayer1().equals(player)) {
                              i = 1;
                              room = r;
                              break;
                         } else if (r.getPlayer2().equals(player)) {
                              i = -1;
                              room = r;
                              break;
                         }
                    }
                    if (room != null && i == room.count) {
                         board = room.getBoard();
                         if (board[x][y] == 0) {
                              board[x][y] = i;
                              room.count *= -1;
                              try {
                                   PrintStream writer = gobang_user_stream.get(room.getPlayer1());
                                   writer.println("Piece " + x + "|" + y + "|" + i);
                                   writer.flush();
                              } catch (Exception e) {
                                   PrintStream writer = gobang_user_stream.get(room.getPlayer2());
                                   writer.println("State Win");
                                   writer.flush();
                              }
                              try {
                                   PrintStream writer = gobang_user_stream.get(room.getPlayer2());
                                   writer.println("Piece " + x + "|" + y + "|" + i);
                                   writer.flush();
                              } catch (Exception e) {
                                   PrintStream writer = gobang_user_stream.get(room.getPlayer1());
                                   writer.println("State Win");
                                   writer.flush();
                              }
                         }
                    }

                    if (isWin(x, y, i, board)) {
                         String toPlayer1 = "State " + (i == 1 ? "Win" : "Lose");
                         String toPlayer2 = "State " + (i == -1 ? "Win" : "Lose");
                         PrintStream writer1 = gobang_user_stream.get(room.getPlayer1());
                         room.setReady1(false);
                         writer1.println(toPlayer1);
                         writer1.flush();
                         PrintStream writer2 = gobang_user_stream.get(room.getPlayer2());
                         room.setReady2(false);
                         writer2.println(toPlayer2);
                         writer2.flush();

                         room.setBoard(new int[15][15]);
                         room.count = 1;
                    }

               } catch (Exception e) {
                    e.printStackTrace();
               }
          }

          private boolean isWin(int x, int y, int player, int[][] board) {
               int tempX = x;
               int tempY = y;
               int checkX[] = { -1, 1, 1, -1, 1, -1, 0, 0 };
               int checkY[] = { -1, 1, -1, 1, 0, 0, 1, -1 };
               int direction = 0;
               int count = 1;

               while (direction < 8 && count < 5) {
                    try {
                         tempX += checkX[direction];
                         tempY += checkY[direction];
                         if (board[tempX][tempY] == player)
                              count++;
                         else {
                              throw new Exception();
                         }
                    } catch (Exception e) {
                         if (direction % 2 == 1)
                              count = 1;
                         tempX = x;
                         tempY = y;
                         direction++;
                    }
               }

               if (count >= 5) {
                    return true;
               }

               return false;
          }

          private void ready(String message) {
               for (Room r : room_vector) {
                    if (r.getPlayer1().equals(message)) {
                         r.setReady1(true);
                         break;
                    } else if (r.getPlayer2().equals(message)) {
                         r.setReady2(true);
                         break;
                    }
               }
          }

     }
}
