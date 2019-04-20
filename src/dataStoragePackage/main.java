package dataStoragePackage;

import java.io.IOException;
import java.util.Scanner;

public class main {

	public static void main(String[] args) throws IOException {
		
		int choice=0;
		//build index file first time in memory
		setIndex firstTime=new setIndex();
		//firstTime.buildfirstTimeIndex();
		
		//create object
		operations obj=new operations();
		while(true) {
			
			
			System.out.println("\nPlease choose an action?");
			System.out.println("1/ Insert new record \n"+"2/ Delete record\n"
					+"3/ Search for record\n"+"4/ Update a record \n"
					+"5/ Show all records\n"+"6/ Show index\n"+"7/ Exit\n");
			choice =new Scanner(System.in).nextInt();
			
			if(choice==1) {
				int id=0,price=0,quentity=0;
				System.out.println("please enter product id");
				id=new Scanner(System.in).nextInt();
				System.out.println("please enter product price");
				price=new Scanner(System.in).nextInt();
				System.out.println("please enter product quentity");
				quentity=new Scanner(System.in).nextInt();
				obj.insert(id, price, quentity);
			}
			else if(choice==2) {
				System.out.println("please enter product id you want to delete");
				int id=new Scanner(System.in).nextInt();
				obj.delete(id);
			}
			else if(choice==3) {
				System.out.println("please enter product id you want to search for");
				int id=new Scanner(System.in).nextInt();
				obj.search(id);
				System.out.println();
			}
			else if(choice==4) {
				int id=0,price=0,quentity=0;
				System.out.println("please enter product id you want to update");
				id=new Scanner(System.in).nextInt();
				
				System.out.println(" update price >>> 1, update quentity >>> 2, both >>> 3");
				choice=new Scanner(System.in).nextInt();
				if(choice ==1) {
					System.out.println("please enter new product price");
					price=new Scanner(System.in).nextInt();
					obj.updatePrice(id, price);
				}
				else if(choice==2) {
					System.out.println("please enter new product quentity");
					quentity=new Scanner(System.in).nextInt();
					obj.updateQuentity(id, quentity);
				}
				else
				{	
					System.out.println("please enter new product price");
					price=new Scanner(System.in).nextInt();
					System.out.println("please enter new product quentity");
					quentity=new Scanner(System.in).nextInt();
					obj.update(id, price, quentity);

				}
				
			}
			else if(choice==5) 
				obj.printData();
			else if(choice==6)
				firstTime.readSomeData();
			else if(choice==7)
				break;
			else 
				System.out.println("please enter a valid choice");
		}
	}

}
