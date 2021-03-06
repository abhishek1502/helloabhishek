package cloudsharing;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class UtilityClass {

    public static ArrayList<String> ListOfUser() {
        String buf = "";
        ArrayList<String> FilList = new ArrayList<String>();
        try {
            Connection con = getconnect();
            Statement st = con.createStatement();
            //String str="select * from tblFile ";
            String str1 = "SELECT Id FROM tbl_register";
            ResultSet rs = st.executeQuery(str1);
//            int i=1;
            while (rs.next()) {
                FilList.add(rs.getString(1));
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FilList;
    }


//list of md5sum 
    public static ArrayList<String> ListOfMd5() {
        String buf = "";
        ArrayList<String> md5sumlist = new ArrayList<String>();
        try {
            Connection con = getconnect();
            Statement st = con.createStatement();
            //String str="select * from tblFile ";
            String str1 = "SELECT md5sum FROM tbl_upload";
            ResultSet rs = st.executeQuery(str1);
//            int i=1;
            while (rs.next()) {
                md5sumlist.add(rs.getString(1));
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return md5sumlist;
    }



    public static ArrayList<String> FilesOfUsers(String UId) {

        String buf = "";
        ArrayList<String> FilList = new ArrayList<String>();
        try {
            Connection con = getconnect();
            Statement st = con.createStatement();
            //String str="select * from tblFile ";
            String str1 = "select Fname from tbl_upload where Uname='" + UId + "'";
            ResultSet rs = st.executeQuery(str1);
//            int i=1;
            while (rs.next()) {
                FilList.add(rs.getString(1));
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FilList;
    }

    public static Connection getconnect() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.4.166.2:3306/reset", "adminRGkDl7W", "kdVtQ6nzwY56");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return con;
    }

    public static int Login(String UserId, String Pass) {

        Connection con = null;

        int i = 0;
        try {
            con = UtilityClass.getconnect();
            System.out.println("connected");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from tbl_register where Id='" + UserId + "' and Pass='" + Pass + "'");

            if (rs.next()) {
                i = 1;
            } else {
                i = 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public static boolean Register(String Name, String Email, String Pass, String Contact) {

        Connection con = null;
        boolean b = false;
        try {
            con = UtilityClass.getconnect();
            System.out.println("connection successfull");
            Statement st = con.createStatement();
            boolean rs = st.execute("INSERT INTO tbl_register VALUES('" + Name + "','" + Email + "','" + Pass + "','" + Contact + "')");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;

    }

    public static boolean Upload(byte[] b, String fname, String Uname) {
        boolean R = false;
        try {
            File myfold = new File(System.getenv("OPENSHIFT_DATA_DIR"));
            if (myfold.exists() == true) {
            } else {
                myfold.mkdir();
            }
            try {
                String fname1 = fname.substring(fname.lastIndexOf(File.separator) + 1);

                File f = new File(System.getenv("OPENSHIFT_DATA_DIR") + File.separator + fname1);

                FileOutputStream fout = new FileOutputStream(f);
                fout.write(b);
                fout.flush();
                fout.close();
                Connection con = getconnect();
                Statement st = con.createStatement();
                boolean rs = st.execute("INSERT INTO tbl_upload VALUES('" + fname1 + "','" + Uname + "')");
                con.close();

                R = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return R;
    }

    public static byte[] fileRead(String fname) {
        byte[] barr = null;
        try {
            File f = new File(fname);
            int l = (int) f.length();
            barr = new byte[l];
            // byte[] b = equalbyte(barr);
//        for (int i = 0; i <b.length; i++) {
//        System.out.println(""+b[i]);
//        }

            FileInputStream fin = new FileInputStream(f);
            DataInputStream din = new DataInputStream(fin);
            //din.readFully(barr, 0, l);
            din.read(barr, 0, l);
            din.close();
            fin.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return barr;
    }

    public static boolean writeFile(byte[] content, String filename) {
        boolean R = false;
        try {
            File myfold = new File(System.getenv("OPENSHIFT_DATA_DIR") +File.separator );
     
            try {
                
                String fname1 = filename.substring(filename.lastIndexOf(",") + 1);

                File f = new File(System.getenv("OPENSHIFT_DATA_DIR") + File.separator + fname1);

                FileOutputStream fout = new FileOutputStream(f);
                fout.write(content);
                fout.flush();
                fout.close();
                R = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return R;

    }

    public static ArrayList<byte[]> Down(String fname) {

        ArrayList<byte[]> b = new ArrayList<byte[]>();
        File f = new File(fname);
        try {
            if (f.exists()) {
                FileInputStream fin = new FileInputStream(f);
                DataInputStream in = new DataInputStream(fin);
                int formDataLength = (int) f.length();

                byte dataBytes[] = new byte[formDataLength];
                int byteRead = 0;
                int totalBytesRead = 0;
                while (totalBytesRead < formDataLength) {
                    byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
                    totalBytesRead += byteRead;
                }

                fin.close();
                // b.ensureCapacity(1);
                b.add(dataBytes);
                return b;

            } else {
                System.out.println("ERROR File Not Found :" + fname);
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    //for shareing  
    public static ArrayList<String> LisstFiles() {
        String buf = "";
        ArrayList<String> FilList = new ArrayList<String>();
        try {
            Connection con = getconnect();
            Statement st = con.createStatement();
            //String str="select * from tblFile ";
            String str1 = "select * from tbl_upload";
            ResultSet rs = st.executeQuery(str1);
//            int i=1;
            while (rs.next()) {
                FilList.add(rs.getString(1));
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FilList;
    }

    // for select user to share 
    public static int FilShare(String Fname, String Uname) {
        int rs = 0;
        Connection con = null;
        boolean b = false;
        try {
            con = UtilityClass.getconnect();
            System.out.println("connection successfull");
            Statement st = con.createStatement();
 //           rs = st.execute("INSERT INTO tbl_share VALUES('" + Fname + "','" + Uname + "')");
rs  =st.executeUpdate("INSERT INTO tbl_share VALUES('" + Fname + "','" + Uname + "')");
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rs;

    }

}
