package dataStoragePackage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class setIndex {
	
	ArrayList<Record> index=new ArrayList<Record>();
	int looper=0;
	RandomAccessFile dataFile;
	RandomAccessFile indexFile;
	
	public setIndex() throws FileNotFoundException {
		
	}
	public void readPrimaryKeys() throws IOException {
		dataFile =new RandomAccessFile("SampleDataFile.bin","rw");
		while(dataFile.getFilePointer()!=dataFile.length()) {
			Record record=new Record();
			record.offset=(int) dataFile.getFilePointer();
			record.product_id= dataFile.readInt();
			if(record.product_id!=0)
				index.add(record);
		}
		dataFile.close();
	}
	
	public void sortInitialIndex() {
		
		Record value=new Record();
		int hole;
		for(int i=1;i<this.index.size();i++) {
			value=this.index.get(i);
			hole=i;
			while(hole>0 && this.index.get(hole-1).product_id>value.product_id) {
				this.index.set(hole, this.index.get(hole-1));
				hole--;
			}
			this.index.set(hole,value);
		}
	}
	
	public void writeToIndex() throws IOException {
		indexFile =new RandomAccessFile("index.txt","rw");
		for(int i=0;i<this.index.size();i++) {
			this.indexFile.writeInt(this.index.get(i).product_id);
			this.indexFile.writeInt(this.index.get(i).offset);
		}
		this.indexFile.close();
	}
	
	public void buildfirstTimeIndex() throws IOException {
		this.readPrimaryKeys();
		this.sortInitialIndex();
		this.writeToIndex();
		this.looper=0;
		this.index.clear();
	}
	public void readSomeData() throws IOException {
		this.indexFile =new RandomAccessFile("index.txt","rw");
		while(indexFile.getFilePointer()!=indexFile.length()) {
			System.out.println("ID: " + this.indexFile.readInt() + " " + "off: " + this.indexFile.readInt());
		}
		indexFile.close();
	}
}
