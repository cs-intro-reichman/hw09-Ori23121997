/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }
    
    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() {
        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData newCharData = new CharData(chr);
        Node newNode = new Node(newCharData , first);
        first = newNode;
        size++;
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
        if (size ==0) {
            return "";
        }
        Node currnet = first;
        String res = "(";
        while (currnet != null) {
            res = res + currnet.cp.toString() + " ";
            currnet = currnet.next;
        }
        return res.substring(0, res.length()-1)+")";
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        int i = 0;
        Node pointer = first;
        while (pointer != null) {
            if (pointer.cp.chr == chr) {
                return i;
            }
            else {
                i++;
                pointer = pointer.next;
            }
        } 
        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        if (indexOf(chr) == -1 ) {
            addFirst(chr);
        }else if (first.cp.chr == chr) {
            first.cp.count++;
        }
        else{
        ListIterator it = listIterator(indexOf(chr));
        it.current.cp.count++;
        }
    }


    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        if (first == null) {
            return false;
        }
        if (first.cp.chr == chr) {
            first = first.next;
            size--;
            return true;
        }
        Node currnet = first;
        while (currnet.next != null) {
            if (currnet.next.cp.chr == chr) {
                currnet.next = currnet.next.next;
                size--;
                return true;
            }
            else {
                currnet = currnet.next;
            }
        }
        return false;
    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return first.cp;
        }
        int i = 1;
        Node currnet = first.next;
        while (i != index) {
            currnet = currnet.next;
            i++;
        }

        return currnet.cp;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) return null;
	    // Gets the element in position index of this list
	    Node current = first;
	    int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }
}