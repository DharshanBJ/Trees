import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class splayNode{
    int key;
    int value;
    splayNode left;
    splayNode right;
    splayNode(int k,int val){
        key=k;
        value=val;
    }
}

public class SplayTrees {


    /** Get user time in nanoseconds. */
    public static long getUserTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadUserTime( ) : 0L;
    }

    splayNode root;

    splayNode rightRotate(splayNode x){

        splayNode y=x.left;
        x.left=y.right;
        y.right=x;
        //return the new node
        return y;

    }

    //function to left rotate subtree rooted at y
    splayNode leftRotate(splayNode x){

        splayNode y = x.right;
        x.right = y.left;
        y.left = x;
        return y;

    }

    splayNode splay(splayNode root,int key){


        if(root==null || root.key==key){
            return root;
        }


        if(key<root.key){

            if(root.left == null){
                return root;
            }
            if(key < root.left.key){

                root.left.left=splay(root.left.left,key);

                root=rightRotate(root);


            }
            else if (root.left.key < key) // Zig-Zag (Left Right)
            {
                // First recursively bring the key as root of left-right
                root.left.right = splay(root.left.right, key);

                // Do first rotation for root->left
                if (root.left.right != null) {
                    root.left = leftRotate(root.left);
                }
            }

            //second rotation for root
            return (root.left==null)? root:rightRotate(root);

        }else{
            if (root.right == null) return root;

            // Zig-Zag (Right Left)
            if (root.right.key > key)
            {

                root.right.left = splay(root.right.left, key);


                if (root.right.left != null)
                    root.right = rightRotate(root.right);
            }
            else if (root.right.key < key)// Zag-Zag (Right Right)
            {

                root.right.right = splay(root.right.right, key);
                root = leftRotate(root);
            }

            // Do second rotation for root
            return (root.right == null)? root: leftRotate(root);

        }
    }

    // Function to insert a new key k in splay tree with given root
    splayNode insert(splayNode root, int k,int val)
    {
        // Simple Case: If tree is empty
        if (root == null) return new splayNode(k,val);

        // Bring the closest leaf node to root
        root = splay(root, k);

        // If key is already present, then return
        if (root.key == k) return root;

        // Otherwise allocate memory for new node
        splayNode newnode  = new splayNode(k,val);

        // If root's key is greater, make root as right child
        // of newnode and copy the left child of root to newnode
        if (root.key > k)
        {
            newnode.right = root;
            newnode.left = root.left;
            root.left = null;
        }

        // If root's key is smaller, make root as left child
        // of newnode and copy the right child of root to newnode
        else
        {
            newnode.left = root;
            newnode.right = root.right;
            root.right = null;
        }

        return newnode; // newnode becomes new root
    }

    // The delete function for Splay tree. Note that this function
    // returns the new root of Splay Tree after removing the key
    splayNode delete(splayNode root, int key)
    {
        splayNode temp;
        if (root==null)
            return null;

        // Splay the given key
        root = splay(root, key);

        // If key is not present, then
        // return root
        if (key != root.key)
            return root;

        // If key is present
        // If left child of root does not exist
        // make root->right as root
        if (root.left==null)
        {
            temp = root;
            root = root.right;
        }

        // Else if left child exits
        else
        {
            temp = root;


            root = splay(root.left, key);

            root.right = temp.right;
        }


        return root;

    }


    int search(splayNode root,int key){
        return splay(root,key).value;
    }

    public static void main(String[] args){
        SplayTrees tree=new SplayTrees();

        int count=100000;

     double startUserTimeNano = getUserTime();

        for(int i=0;i<count;i++) {
            int randomValue = ThreadLocalRandom.current().nextInt(0, 500 + 1);
            int randomKey = ThreadLocalRandom.current().nextInt(0, 100 + 1);
            tree.root=tree.insert(tree.root,5*i,randomValue);

        }

//        double startUserTimeNano = getUserTime();
//
//        for(int i=0;i<count;i++){
////            int randomKey = ThreadLocalRandom.current().nextInt(0, 100 + 1);
//            tree.search(tree.root,5*i);
//        }

        double taskUserTimeNano = getUserTime() - startUserTimeNano;
        System.out.println("User time for  "+count+" search is "+taskUserTimeNano/1000000000+ "  seconds");


//        for(int i=0;i<100;i++) {
//            int randomValue = ThreadLocalRandom.current().nextInt(0, 500 + 1);
//            int randomKey = ThreadLocalRandom.current().nextInt(0, 100 + 1);
//            sp.insert(sp.root,randomValue,randomKey);
//        }
//        System.out.println(sp.search(sp.root,19));

    }

}
