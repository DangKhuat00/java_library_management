package librarymanage.java_library_management;

import java.util.ArrayList;
import java.util.Scanner;

public class testThu4chucnangdau {
    public static void main(String[] args) {
        documentManager dm = new documentManager();
        Scanner sc =  new Scanner(System.in);
        int luachon;
        do {
            System.out.println("Chon chuc nang duoi day: ");
            System.out.println("1.Them tai lieu \n2.xoa tai lieu \n3.Sua tai lieu \n4.Tim kiem tai lieu \n5.Hien thi nhung tai lieu o thoi diem hien tai \n0.Thoat");
            luachon = sc.nextInt();
            switch (luachon) {
                case 1:
                    dm.addDocument();
                    break;
                case 2:
                    dm.deleteDocument();
                    break;
                case 3:
                    dm.updateDocument();
                    break;
                case 4:
                    dm.findDocument();
                    break;
                case 5:
                    for(Document doc: dm.getDocuments()){
                        doc.printFor();
                    }
                    break;
            }
        }while(luachon != 0);
        ArrayList<Document> library = dm.getDocuments();
        for(Document doc: library){
            doc.printFor();
        }
    }


}
