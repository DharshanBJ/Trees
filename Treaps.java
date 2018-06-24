
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Treaps {

    class TreapNode{
        TreapNode left;
        TreapNode right;
        int key;
        int value;
        int pri;

        TreapNode(int k,int val){
            key=k;
            value=val;

        }
    }

    /** Get user time in nanoseconds. */
    public static long getUserTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadUserTime( ) : 0L;
    }

    TreapNode root;

    //function to right rotate subtree rooted with z
    TreapNode rightRotate(TreapNode x){

        TreapNode y=x.left;
        x.left=y.right;
        y.right=x;
        //return the new node
        return y;

    }

    //function to left rotate subtree rooted at y
    TreapNode leftRotate(TreapNode x){

        TreapNode y = x.right;
        x.right = y.left;
        y.left = x;
        return y;

    }

/* Recursive implementation of insertion in Treap */
    TreapNode insert(TreapNode root, int key,int value)
    {
        // If root is NULL, create a new node and return it
        if (root==null)
            return new TreapNode(key,value);

        // If key is smaller than root
        if (key <= root.key)
        {
            // Insert in left subtree
            root.left = insert(root.left, key,value);

            // Fix Heap property if it is violated
            if (root.left.pri > root.pri)
                root = rightRotate(root);
        }
        else  // If key is greater
        {
            // Insert in right subtree
            root.right = insert(root.right, key,value);

            // Fix Heap property if it is violated
            if (root.right.pri > root.pri)
                root = leftRotate(root);
        }
        return root;
    }

    int search(TreapNode root, int key)
    {
        // Base Cases: root is null or key is present at root
        if (root == null ){

            System.out.println("Key doesn't exist");
            return -1;

        }else if(root.key==key){

            return root.value;

        }

        // Key is greater than root's key
        if (root.key < key)
            return search(root.right, key);

        // Key is smaller than root's key
        return search(root.left, key);
    }

    /* Recursive implementation of Delete() */
    TreapNode deleteNode(TreapNode root, int key)
    {
        if (root == null)
            return root;

        if (key < root.key)
            root.left = deleteNode(root.left, key);
        else if (key > root.key)
            root.right = deleteNode(root.right, key);

            // IF KEY IS AT ROOT

            // If left is NULL
        else if (root.left == null)
        {
            TreapNode temp = root.right;
//            deleteNode(root,key);//?
            root = temp;  // Make right child as root
        }

        // If Right is NULL
        else if (root.right == null)
        {
            TreapNode temp = root.left;
//            deleteNode(root,key);//?
            root = temp;  // Make left child as root
        }

        // If ksy is at root and both left and right are not NULL
        else if (root.left.pri < root.right.pri)
        {
            root = leftRotate(root);
            root.left = deleteNode(root.left, key);
        }
        else
        {
            root = rightRotate(root);
            root.right = deleteNode(root.right, key);
        }

        return root;
    }

    public static void main(String[] args){
        Treaps tree=new Treaps();
        int count=100000;

//      double startUserTimeNano = getUserTime();

        for(int i=0;i<count;i++) {
            int randomValue = ThreadLocalRandom.current().nextInt(0, 500 + 1);
            int randomKey = ThreadLocalRandom.current().nextInt(0, 100 + 1);
            tree.root=tree.insert(tree.root,5*i,randomValue);

        }

        double startUserTimeNano = getUserTime();

        for(int i=0;i<count;i++){
            tree.deleteNode(tree.root,5*i);
        }

        double taskUserTimeNano = getUserTime() - startUserTimeNano;
        System.out.println("User time for  "+count+" searches is "+taskUserTimeNano/1000000000+ "  seconds");

    }

}
