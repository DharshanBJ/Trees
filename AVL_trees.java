
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class TreeNodeAVL{
    TreeNodeAVL left;
    TreeNodeAVL right;
    int key;
    int value;
    int height;
    public TreeNodeAVL(int k,int val){ //initially when you create a node,its height will be 1
        key=k;
        value=val;
        height=1;
    }
}

public class AVL_trees {

    TreeNodeAVL root;
    //height of a tree is the number of nodes on the longest path from root to a leaf
    //by this convention,the height of a tree is 1 greater than the maximum heights of the two subtrees

    /** Get user time in nanoseconds. */
    public static long getUserTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadUserTime( ) : 0L;
    }

    //function to get height of tree
    int height(TreeNodeAVL node){
        if(node==null)return 0;

        return node.height;
    }

    //function to right rotate subtree rooted with z
    TreeNodeAVL rightRotate(TreeNodeAVL z){
        TreeNodeAVL node1=z.left;
        TreeNodeAVL node2=node1.right;

        //perform rotation
        node1.right=z;
        z.left=node2;

        //update heights
        z.height=Math.max(height(z.left),height(z.right))+1;
        node1.height=Math.max(height(node1.left),height(node1.right))+1;

        //return the new node
        return node1;

    }
    void create(int key,int value){
        root=new TreeNodeAVL(key,value);
    }
    //function to left rotate subtree rooted at y
    TreeNodeAVL leftRotate(TreeNodeAVL y){
        TreeNodeAVL node1=y.right;
        TreeNodeAVL node2=node1.left;

        //perform rotation
        node1.left=y;
        y.right=node2;

        //update heights
        node1.height=Math.max(height(node1.left),height(node1.right))+1;
        y.height=Math.max(height(y.left),height(y.right))+1;

        //return new root
        return node1;
    }

    //balance factor
    int balanceFactor(TreeNodeAVL node){
        if(node==null)return 0;
        return height(node.left) - height(node.right);
    }

    TreeNodeAVL insert(TreeNodeAVL node,int key,int value){

        //base case
        if(node==null)return (new TreeNodeAVL(key,value));

        //perform standard insertion
        if(key<node.key){
            node.left=insert(node.left,key,value);
        }else if(key>node.key){
            node.right=insert(node.right,key,value);
        }else return node;

        //update height of this node
        node.height=1+Math.max(height(node.left),height(node.right));

        int balance=balanceFactor(node);

        //left left case
        if(balance > 1 && key < node.left.key){
            return rightRotate(node);
        }

        //right right case
        if(balance < -1 && key > node.right.key){
            return leftRotate(node);
        }

        //left right case
        if(balance > 1 && key > node.left.key){
            node.left=leftRotate(node.left);
            return rightRotate(node);
        }

        //right left case
        if(balance<-1 && key<node.right.key){
            node.right=rightRotate(node.right);
            return leftRotate(node);
        }

        //return the unchanged node
        return node;
    }

    //search function
    int search(TreeNodeAVL root,int k){
        // Base Cases: root is null or key is present at root
        if (root == null ){

            System.out.println("Key doesn't exist");
            return -1;

        }else if(root.key==k){

            return root.value;

        }

        // Key is greater than root's key
        if (root.key < k)
            return search(root.right, k);

        // Key is smaller than root's key
        return search(root.left, k);
    }

    //delete function
    TreeNodeAVL delete(TreeNodeAVL root,int key){

        //perform standard BST delete
          /* Base Case: If the tree is empty */
        if (root == null)  return root;

        /* Otherwise, recur down the tree */
        if (key < root.key)
            root.left = delete(root.left, key);
        else if (key > root.key)
            root.right = delete(root.right, key);

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
            root.right = delete(root.right, root.key);


        }

        //if the tree had only one node,then return
        if(root==null)return root;

        //update height of this node
        root.height=1+Math.max(height(root.left),height(root.right));

        int balance=balanceFactor(root);

        //left left case
        if(balance > 1 && key < root.left.key){
            return rightRotate(root);
        }

        //right right case
        if(balance < -1 && key > root.right.key){
            return leftRotate(root);
        }

        //left right case
        if(balance > 1 && key > root.left.key){
            root.left=leftRotate(root.left);
            return rightRotate(root);
        }

        //right left case
        if(balance<-1 && key<root.right.key){
            root.right=rightRotate(root.right);
            return leftRotate(root);
        }

        //return the unchanged node
        return root;

    }

    public static int minValue(TreeNodeAVL root)
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
        AVL_trees tree=new AVL_trees();

        int count=10000;

     double startUserTimeNano = getUserTime();

        for(int i=0;i<count;i++) {
            int randomValue = ThreadLocalRandom.current().nextInt(0, 500 + 1);
            int randomKey = ThreadLocalRandom.current().nextInt(0, 100 + 1);
            tree.root=tree.insert(tree.root,randomKey,randomValue);

        }
//
//      double startUserTimeNano = getUserTime();
//
//        for(int i=0;i<count;i++){
//            int randomKey = ThreadLocalRandom.current().nextInt(0, 100 + 1);
//            tree.delete(tree.root,5*i);
//        }

        double taskUserTimeNano = getUserTime() - startUserTimeNano;
        System.out.println("User time for  "+count+" delete is "+taskUserTimeNano/1000000000+ "  seconds");


//        System.out.println(tree.search(tree.root,3829));

    }
}
