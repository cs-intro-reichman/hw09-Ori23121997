public class Main {
    public static void main(String[] args){
        List list = new List();
        list.update('k');
        list.update('o');
        System.out.println(list.getSize());
        list.update('g');
        list.update('l');
        list.update('k');
        list.update('s');
        list.remove('g');
        System.out.println(list.getSize());
        System.out.println(list.toString());
        System.out.println(list.indexOf('s'));
        System.out.println(list.toString());
        list.update('k');
        System.out.println(list.toString());
    }
}
