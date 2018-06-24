import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class UnbalancedBST {

    static class TreeNode {
        TreeNode right;
        TreeNode left;
        int value;
        int key;
        public TreeNode(int val,int k){
            value=val;
            key=k;
        }
    }

    /** Get user time in nanoseconds. */
    public static long getUserTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadUserTime( ) : 0L;
    }

    public static TreeNode root;

    public static void Create(int key,int value){
         root=new TreeNode(key,value);
    }

//    public static void Insert(TreeNode root,int val,int key){
//
//        while(root != null) {
//            if (key < root.key) {
//                //go to
//                // the left sub tree
//
//                if(root.left==null){
//                    TreeNode node=new TreeNode(val,key);
//                    root.left=node;
//                    break;
//                }
//                root=root.left;
//
//            } else if (key > root.key) {
//                //go to the right sub tree
//                if(root.right==null){
//                    TreeNode node=new TreeNode(val,key);
//                    root.right=node;
//                    break;
//                }
//                root=root.right;
//            }
//        }
//
//    }
    public static TreeNode insert(TreeNode root,int key,int value){
        if(root==null){
            root=new TreeNode(key,value);
            return root;
        }

        /* Otherwise, recur down the tree */
        if (key < root.key)
            root.left = insert(root.left, key,value);
        else if (key > root.key)
            root.right = insert(root.right, key,value);

        /* return the (unchanged) node pointer */
        return root;
    }

    public static TreeNode Search(TreeNode root,int key){
        // Base Cases: root is null or key is present at root
        if (root==null || root.key==key)
            return root;

        // val is greater than root's key
        if (root.key > key)
            return Search(root.left, key);

        // val is less than root's key
        return Search(root.right, key);
    }

//    public static void Delete(TreeNode root,int key){
//
//     while(root!=null){
//         if(root.key==key){
//             //left child is null
//             if(root.left==null){
//                 //replace root by root's right child
//
//             }
//             //right child is null
//             if(root.right==null){
//                 root=root.left;
//             }
//             //neither left nor right child is null
//         }
//     }
//    }
   public static void Delete(TreeNode root,int key)
    {
      root = deleteRec(root, key);
    }

    /* A recursive function to insert a new key in BST */
    public static TreeNode deleteRec(TreeNode root, int key)
    {
        /* Base Case: If the tree is empty */
        if (root == null)  return root;

        /* Otherwise, recur down the tree */
        if (key < root.key)
            root.left = deleteRec(root.left, key);
        else if (key > root.key)
            root.right = deleteRec(root.right, key);

            // if key is same as root's key, then This is the node
            // to be deleted
        else
        {
            // node with only one child or no child
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;

            // node with two children: Get the inorder successor (smallest
            // in the right subtree)
            root.key = minValue(root.right);

            // Delete the inorder successor
            root.right = deleteRec(root.right, root.key);


        }

        return root;
    }

    public static int minValue(TreeNode root)
    {
        int minv = root.key;
        while (root.left != null)
        {
            minv = root.left.key;
            root = root.left;
        }
        return minv;
    }

    public static void main(String[] args){
        //create an empty BST-i.e,it creates a root node
        Create(4,8);
        //insert elements into a BST


//        Insert(root, 7, 9);
//        Insert(root, 4, 90);
//        Insert(root, 3, 32);
//        Insert(root, 32, 12);
//        Insert(root, 2, 14);
//        Insert(root, 98, 66);

        int count=100;
//        double startUserTimeNano = getUserTime();

        for(int i=0;i<count;i++) {
            int randomValue = ThreadLocalRandom.current().nextInt(0, 500 + 1);
            int randomKey = ThreadLocalRandom.current().nextInt(0, 100 + 1);
            insert(root,5*i,randomValue);

        }

        double startUserTimeNano = getUserTime();

        for(int i=0;i<count;i++){
            Delete(root,5*i);
        }

        double taskUserTimeNano = getUserTime() - startUserTimeNano;
        System.out.println("User time for  "+count+" insertions is "+taskUserTimeNano/1000000000+ "  seconds");

//        System.out.println(Search(root,66));
//        Delete(root,12);
//        System.out.println(Search(root,12));

    }
}
