import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.RandomAccess;


class key_offset implements Comparable<key_offset>{
	int key,offset,filenum;

	public key_offset(int key, int offset, int filenum) {
		super();
		this.key = key;
		this.offset = offset;
		this.filenum = filenum;
	}

	public int compareTo(key_offset arg0) {
		// TODO Auto-generated method stub
		if(this.key==arg0.key){return 0;}
		return this.key>arg0.key?1:-1;
	}
	
	
	
}

public class ExternalMergeSorter {
	String base_name = "myfile";
	String file_name;
	int readIndex;
	
	public void setReadIndex(int x){
		readIndex = x;
	}
	
	public void mergeFiles(int numFiles, String resFile) throws IOException{
		//buffer size = 1 leke kar raha hu
		BufferedReader[] brs = new BufferedReader[numFiles];
		PrintWriter pr = new PrintWriter(new FileWriter(resFile));
		int i = 0;
		for(i=0;i<numFiles;i++){
			brs[i] = new BufferedReader(new FileReader(base_name+"_"+i));
		}
		PriorityQueue<key_offset> minheap = new PriorityQueue<key_offset>();
		for(i=0;i<numFiles;i++){
			String str = brs[i].readLine();
			if(str!=null){
				minheap.add(new key_offset(
						Integer.parseInt(str.split(",")[0]), 
						Integer.parseInt(str.split(",")[1]), 
						i));
			}
		}
		while(!minheap.isEmpty()){
			key_offset ko = minheap.poll();
			pr.println(ko.key+","+ko.offset);
			System.out.println("Filenumber:"+ko.filenum);
			String str = brs[ko.filenum].readLine();
			if(str!=null){
				minheap.add(new key_offset(
						Integer.parseInt(str.split(",")[0]), 
						Integer.parseInt(str.split(",")[1]), 
						ko.filenum));
			}
		}
		for(i=0;i<numFiles;i++){
			brs[i].close();
		}
		pr.close();
	}
	
	public int readNVals(int n, String filename) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String str = "";
		str = br.readLine();
		int file_no = 0,offset = 0;
		while(str!=null){
//			ArrayList<Integer> nkeys = new ArrayList<Integer>();
//			ArrayList<Integer> offsets = new ArrayList<Integer>();
			ArrayList<key_offset> key_off = new ArrayList<key_offset>();
			int i = 0;
			while(i<n && str!=null){
//				System.out.print(str.split(",")[0]);
//				nkeys.add(Integer.parseInt(str.split(",")[0]));
//				offsets.add(offset);
				key_off.add(new key_offset(Integer.parseInt(str.split(",")[0]), offset,file_no));
				offset+=str.length()+1;
				str = br.readLine();
				i++;
			}
			Collections.sort(key_off);
			PrintWriter pr = new PrintWriter(new FileWriter(base_name+"_"+file_no));
			for(key_offset ko: key_off){
				pr.println(ko.key+","+ko.offset);
//				System.out.println(ko.key+","+ko.offset);
			}
			pr.close();
			file_no++;
		}
		br.close();
		return file_no;
	}
	
}
