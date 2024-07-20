import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

class FileSystem{
    File root;
    String path;
    Scanner s;
    FileSystem(File root){
        this.root = root;
        this.path = root.getPath();
        this.s = new Scanner(System.in);
    }
    public void addFolder(){
        System.out.println("Введите название папки");
        String name=null;
        while(name==null){
            name=s.nextLine();
        }
        File file = new File(path, name);
        file.mkdir();
    }
    public void addfile() throws IOException {
        System.out.println("Введите название папки");
        String name=null;
        while(name==null){
            name=s.nextLine();
        }
        File file = new File(path, name);
        file.createNewFile();
    }
    public void open(){
        File[] list = root.listFiles();
        for (File tmp : list) {
            if (tmp.isDirectory()) {
                System.out.println(tmp.getName() + "                             " + getDate() + "    " + tmp.length()+ " bite                file");
                FileSystem j = new FileSystem(tmp);
            } else {
                System.out.println(tmp.getName() + "                             " + getDate() + "    " + tmp.length() + " bite                folder");
            }
        }
    }

    public void recourse_open(int level){
        File[] list = root.listFiles();
        for (File tmp : list) {
            for (int i = 0; i < level; i++) {
                System.out.print("-");
            }
            if (tmp.isDirectory()) {
                System.out.println(tmp.getName() + "                             " + getDate() + "    " + tmp.length()+ " bite                file");
                FileSystem j = new FileSystem(tmp);
                j.recourse_open(level + 1);
            } else {
                System.out.println(tmp.getName() + "                             " + getDate() + "    " + tmp.length() + " bite                folder");
            }
        }
    }

    public void recourse_open(int level, int deep){
        if(deep!=0) {
            File[] list = root.listFiles();
            for (File tmp : list) {
                for (int i = 0; i < level; i++) {
                    System.out.print("-");
                }
                if (tmp.isDirectory()) {
                    System.out.println(tmp.getName() + "                             " + getDate() + "    " + tmp.length()+ " bite                file");
                    FileSystem j = new FileSystem(tmp);
                    deep--;
                    j.recourse_open(level + 1, deep);
                } else {
                    System.out.println(tmp.getName() + "                             " + getDate() + "    " + tmp.length() + " bite                folder");
                }
            }
        }
    }

    public String getDate(){
        Date date = new Date(root.lastModified());
        SimpleDateFormat sdf = new SimpleDateFormat("d.M.y h:mm,a", Locale.ENGLISH);
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public void rename() throws IOException {
        System.out.println("Введите новое название папки");
        String name=null;
        while(name==null){
            name=s.nextLine();
        }
        String path = root.getParent() +"\\" + name;
        File file = new File(path);
        if(root.renameTo(file)){
            root = file;
            this.path = path;
            System.out.println("Новый путь к файлу/папке:\n" + path);
        }else{
            System.out.println("По какой-то причине операция не завершилась успехом. Проверьте, существуют ли другие файлы с таким же именем и расположением.");
        }

    }

    public void delete(){
        if(!root.delete()){
            System.out.println("В папке содержаться файлы. Вы уверены, что хотите удалить папку и ее содержимое?\n  1 - Да\n  2 - Нет");
            int input = s.nextInt();
            if(input == 1){
                recourse_del(root);
                root.delete();
            }
        }
    }

    public void recourse_del(File file){
        if(!file.delete()) {
            File[] list = file.listFiles();
            for (File tmp : list) {
                recourse_del(tmp);
            }
            root.delete();
        }
    }

    public void contains(){
        File [] list = root.listFiles();
        int folders = 0;
        int files = 0;
        for(File tmp: list){
            if(tmp.isDirectory()){
                folders++;
            }else{
                files++;
            }
        }
        System.out.println(root.getName() + "                 " + folders + " folders, " + files + " files");
    }
    public List<File> search(String key){
        File[] list = root.listFiles();
        List<File> result= new ArrayList<>();
        String name;
        boolean poisk=false;
        for (int i = 0; i <key.length() ; i++) {
            if(key.charAt(i)=='*'){
                poisk=true;
            }
        }
        for(File tmp: list){
            name = tmp.getName();
            int j=0;
            int k=0;
            int num=0;
            while(k<name.length() && j<key.length()){
                switch (key.charAt(j)){
                    case('_'):
                        j++;
                        k++;
                        num++;
                        break;
                    case('*'):
                        num++;
                        j++;
                        if(j+1 <key.length()){
                            while(k<name.length() && name.charAt(k)!=key.charAt(j+1)){
                                k++;
                            }
                            if(k!=name.length()){
                                k--;
                            }
                        }
                        break;
                    default:
                        if(key.charAt(j)==name.charAt(k)){
                            num++;
                            k++;
                            j++;
                        }else{
                            k=name.length();
                        }
                        break;
                }
            }
            if(poisk == true && num==key.length()){
                result.add(tmp);
            }
            if(poisk == false && num==key.length() && num == name.length()){
                result.add(tmp);
            }
        }
        return result;
    }


}
public class Main {

    public static void main(String[] args) throws IOException {
        boolean program = true;
        Scanner s = new Scanner(System.in);
        String path="";
        System.out.println("Введите путь к папке");
        path = s.nextLine();
        File root = new File(path);
        FileSystem fileSystem = new FileSystem(root);
        int input=0;
        fileSystem.contains();
        while(program){
            System.out.println("0 - выход из программы\n1 - добавить папку\n2 - добавить файл\n3 - удалить\n4 - переименовать\n5 - просмотр\n6 - сменить папку/файл");
            input = s.nextInt();
            switch(input){
                case(0):
                    program = false;
                    break;
                case(1):
                    fileSystem.addFolder();
                    break;
                case(2):
                    fileSystem.addfile();
                    break;
                case(3):
                    fileSystem.delete();
                    break;
                case(4):
                    fileSystem.rename();
                    break;
                case(5):
                    System.out.println("1 - посмотреть\n2 - посмотреть рекурсивно\n3 - посмотреть с указанием глубины");
                    int in = s.nextInt();
                    switch(in){
                        case(1):
                            fileSystem.open();
                            break;
                        case(2):
                            fileSystem.recourse_open(0);
                            break;
                        case(3):
                            System.out.println("Введите глубину");
                            fileSystem.recourse_open(0, s.nextInt());
                            break;
                    }
                    break;
                case(6):
                    path =null;
                    System.out.println("Введите путь к папке");
                    while(path==null || path==""){
                        path = s.nextLine();
                    }
                    fileSystem = new FileSystem(new File(path));
                    fileSystem.contains();
                    break;
                case(7):
                    String key = null;
                    while(key==null) {
                        key=s.nextLine();
                    }
                    List<File> result = fileSystem.search(s.nextLine());
                    for (File tmp: result){
                        System.out.println(tmp.getName());
                    }
            }
        }

    }
}