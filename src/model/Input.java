package model;

///////////This class is created to deal with input from the dataset
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Input {

    private String filename;
    private FileReader fr;
    private BufferedReader br;
    private static int classlabel;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public boolean setProperty(String name){
		this.filename=name;
		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        return true;
	}
	public String readLine(){
		String line;
	    try {
			if(br.ready()){
					line= br.readLine();	
					classlabel=Integer.valueOf(line.substring(line.length()-1, line.length()));
					//System.out.println(classlabel);
					return line.substring(line.indexOf('<')+1, line.indexOf('>'));
			}else{
				br.close();
				fr.close();
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void write(String path, String content) {
	      String s = new String();
	      String s1 = new String();
	      try {
	       File f = new File(path);
	       if (f.exists()) {
	        System.out.println("The target file exists");
	       } else {
	        System.out.println("The target file is not existing, now creating ...");
	        if (f.createNewFile()) {
	         System.out.println("file created successfully!");
	        } else {
	         System.out.println("file created failed!");
	        }

	       }
	      if(false){
	       BufferedReader input = new BufferedReader(new FileReader(f));

	       while ((s = input.readLine()) != null) {
	        s1 += s + "\n";
	       }
	       input.close();
	      }
	       s1 += content;

	       BufferedWriter output = new BufferedWriter(new FileWriter(f));
	       output.write(s1);
	       output.close();
	      } catch (Exception e) {
	       e.printStackTrace();
	      }
	}
	public static int getClasslabel() {
		return classlabel;
	}
	public static void setClasslabel(int classlabel) {
		Input.classlabel = classlabel;
	}


}
