package dataStoragePackage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class operations {
	
	RandomAccessFile dataFile;
	RandomAccessFile indexFile;
	public operations() throws IOException {
		dataFile =new RandomAccessFile("SampleDataFile.bin","rw");
	}
	
	public int BinarySearch(int key) throws IOException {
		
		indexFile =new RandomAccessFile("index.txt","rw");
		long left=0;
		long right= (this.indexFile.length()/8)-1;
		long mid=0;
		int existedKey=0;
		
		while(left<=right) {
			
			mid=(left+right)/2;
			indexFile.seek(mid*8);
			existedKey=indexFile.readInt();
			
			if(key==existedKey) {
				return indexFile.readInt();
			}
			else if(key>existedKey)
				left=mid+1;
			else
				right=mid-1;
		}
		indexFile.close();
		return -1;
	}
	
	public int getPosition(int key) throws IOException {
		
		indexFile =new RandomAccessFile("index.txt","rw");
		int left=0;
		int right= (int)((this.indexFile.length()/8)-1);
		int mid=0;
		int existedKey=0;
		
		while(left<=right) {
			
			mid=(left+right)/2;
			indexFile.seek(mid*8);
			existedKey=indexFile.readInt();
			
			if(key==existedKey) {
				return -1;
			}
			else if(key>existedKey)
				left=mid+1;
			else
				right=mid-1;
		}
		indexFile.close();
		return mid*8;
	}
	
	public int getPositionTodelete(int key) throws IOException {
		
		indexFile =new RandomAccessFile("index.txt","rw");
		int left=0;
		int right= (int)((this.indexFile.length()/8)-1);
		int mid=0;
		int existedKey=0;
		
		while(left<=right) {
			
			mid=(left+right)/2;
			indexFile.seek(mid*8);
			existedKey=indexFile.readInt();
			
			if(key==existedKey) {
				return mid*8;
			}
			else if(key>existedKey)
				left=mid+1;
			else
				right=mid-1;
		}
		indexFile.close();
		return -1;
	}
	public void search(int product_id) throws IOException {
		this.dataFile =new RandomAccessFile("SampleDataFile.bin","rw");
		int offset=BinarySearch(product_id);
		if(offset!=-1) {
			
			this.dataFile.seek(offset);
			System.out.println("Data about product "+ product_id+" >>>>");
			System.out.println("ID: " + this.dataFile.readInt() + " " + 
			"Price: " + this.dataFile.readInt()+ " " + "Quentity: " + this.dataFile.readInt());
		}
		else
			System.out.println("This product "+ product_id+ " is not exist to be searched!");
	}
	
	public int update(int product_id,int product_price,int product_que) throws IOException {
		this.dataFile =new RandomAccessFile("SampleDataFile.bin","rw");
		
		int offset=BinarySearch(product_id);
		if(offset!=-1) {
			dataFile.seek(offset);
			dataFile.skipBytes(4);
			dataFile.writeInt(product_price);
			dataFile.writeInt(product_que);
			dataFile.close();
			return 1;
		}
		dataFile.close();
		return offset;
	}
	public int updatePrice(int product_id,int product_price) throws IOException {
		this.dataFile =new RandomAccessFile("SampleDataFile.bin","rw");
		
		int offset=BinarySearch(product_id);
		if(offset!=-1) {
			dataFile.seek(offset);
			dataFile.skipBytes(4);
			dataFile.writeInt(product_price);
			dataFile.close();
			return 1;
		}
		dataFile.close();
		return offset;
	}
	public int updateQuentity(int product_id,int product_que) throws IOException {
		this.dataFile =new RandomAccessFile("SampleDataFile.bin","rw");
		
		int offset=BinarySearch(product_id);
		if(offset!=-1) {
			dataFile.seek(offset);
			dataFile.skipBytes(8);
			dataFile.writeInt(product_que);
			dataFile.close();
			return 1;
		}
		dataFile.close();
		return offset;
	}
	
	public void insert(int product_id,int product_price,int product_que) throws IOException {
		int position=getPosition(product_id);
		if(position==-1) 
			System.out.println("Sorry, this id >> "+product_id+ " is already exists!");
		else 
		{
			//write data to data file and get offset of the new record
			dataFile =new RandomAccessFile("SampleDataFile.bin","rw");
			indexFile =new RandomAccessFile("index.txt","rw");
			dataFile.seek(dataFile.length());
			int newOffset=(int) dataFile.getFilePointer();
			dataFile.writeInt(product_id);
			dataFile.writeInt(product_price);
			dataFile.writeInt(product_que);
			
			//last element
			if(position+8==indexFile.length()){
				indexFile.seek(indexFile.length());
				indexFile.writeInt(product_id);
				indexFile.writeInt(newOffset);
			}
			//dealing with index file to insert new record
			indexFile.seek(position);		
			while(indexFile.getFilePointer()!=indexFile.length()) {
				int Id = indexFile.readInt();
				int oldOffset = indexFile.readInt();
				indexFile.seek(indexFile.getFilePointer()-8);
				indexFile.writeInt(product_id);
				indexFile.writeInt(newOffset);
				//update the next record to be written
				product_id=Id;
				newOffset=oldOffset;
			}
			indexFile.writeInt(product_id);
			indexFile.writeInt(newOffset);
		}
		dataFile.close();
		indexFile.close();
	}
	
	public void delete(int product_id) throws IOException {
		int position=getPositionTodelete(product_id);
		int offset=BinarySearch(product_id);
		if(position==-1) 
			System.out.println("Sorry, this id >> "+product_id+ " is not exist to be deleted!");
		else if(position+8==indexFile.length()) {
			indexFile.setLength(indexFile.length()-8);
			System.out.println("Deleted Successfully ");
		}
		else 
		{
			//write 000 to data file instead of real id
			dataFile =new RandomAccessFile("SampleDataFile.bin","rw");
			indexFile =new RandomAccessFile("index.txt","rw");
			dataFile.seek(offset);
			dataFile.writeInt(000);
			//dealing with index file to delete record
			indexFile.seek(position);
			int writerPointer=position;
			int readerPointer=position+8;
			
			while(indexFile.getFilePointer()!=-1) {
				
				indexFile.seek(readerPointer);
				int Id = indexFile.readInt();
				int oldOffset = indexFile.readInt();
				
				indexFile.seek(writerPointer);
				indexFile.writeInt(Id);
				indexFile.writeInt(oldOffset);
				
				writerPointer+=8;
				readerPointer+=8;
				if(readerPointer==indexFile.length())
					break;
			}
			System.out.println("Deleted Successfully ");
			indexFile.setLength(indexFile.length()-8);
		}
		dataFile.close();
		indexFile.close();
	}
	
	public void printData() throws IOException {
		this.dataFile =new RandomAccessFile("SampleDataFile.bin","rw");
		int looper=0;
		while(dataFile.getFilePointer()!=dataFile.length()) {
			int id=dataFile.readInt();
			int price=dataFile.readInt();
			int que=dataFile.readInt();
			if(id!=0)
				System.out.println("ID: "+id+" "+"Price: "+price+" "+"Quentaty: "+que);
		}
		dataFile.close();
	}
}
