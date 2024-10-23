import java.util.Scanner;  // Import Scanner class to take input from the user

public class TestingAVLTree {
    public static void main(String[] args) {
        int key;  // Variable to store the key that will be inserted into the AVL Tree
        Scanner input = new Scanner(System.in);  // Create a Scanner object to read user input
        AVLTree tree = new AVLTree();  // Create an instance of the AVLTree class (our AVL Tree)

        // Infinite loop to continuously take input and insert into the AVL Tree
        while (true) {
            System.out.print("Enter a key (-1 to exit): ");
            key = input.nextInt();  // Read user input for the key

            if (key < 0) break;  // If user enters -1, exit the loop

            tree.root = tree.insert(tree.root, key);  // Insert the key into the AVL tree and update the root

            tree.printTree(tree.root, "", false);  // Print the tree structure after each insertion
        }

        System.out.println("Final Tree");
        tree.printTree(tree.root, "", false);  // Print the final AVL Tree structure
    }
}

class Node {
    int key, height;  // Each node stores the key and the height of the node
    Node left, right;  // Pointers to the left and right child nodes

    // Constructor for Node class, initializes the key and sets height to 0
    Node(int d) {
        key = d;  // Set the node's key value
        height = 0;  // Initialize height as 0 (this can change during tree balancing)
        // No need to initialize left and right as they are automatically set to null
    }
}

class AVLTree {
    Node root;  // Reference to the root of the AVL Tree

    // Helper method to get the height of a node
    int getHeight(Node N) {
        if (N == null) 
            return -1;  // If node is null, return height as -1 (meaning no height)
        return N.height;  // Otherwise, return the height of the node
    }

    // Method to find the maximum of two integers (used to update height)
    public int max(int a, int b) { 
        return (a > b ? a : b);  // Return the larger value between a and b
    }

    // Method to calculate the balance factor of a node (left subtree height - right subtree height)
    public int getBalance(Node N) {
        if (N == null) return 0;  // If the node is null, balance is 0
        return getHeight(N.left) - getHeight(N.right);  // Return the difference in height between left and right subtrees
    }

    // Recursive method to print the AVL Tree in a structured way
    public void printTree(Node node, String prefix, boolean isLeft) {
        if (node != null) {
            // Print the current node's key, balance factor, and height
            System.out.printf("%s%s%d(bal: %d, height: %d)\n", prefix, (isLeft ? "|--" : "|-- "), node.key, getBalance(node), getHeight(node));

            // Recursively print the left and right subtrees
            printTree(node.left, prefix + (isLeft ? "|  " : "   "), true);  // Print left subtree
            printTree(node.right, prefix + (isLeft ? "|  " : "   "), false);  // Print right subtree
        }
    }

    // Recursive method to insert a new key into the AVL Tree and return the updated node
    public Node insert(Node node, int key) {
        // Step 1: Perform the normal Binary Search Tree insertion
        if (node == null)
            return (new Node(key));  // If the current node is null, create a new node with the given key
        
        if (key < node.key)  // If key is less than the current node's key, insert in the left subtree
            node.left = insert(node.left, key);
        else if (key > node.key)  // If key is greater, insert in the right subtree
            node.right = insert(node.right, key);
        else  // Duplicate keys are not allowed in the AVL Tree
            return node;

        // Step 2: Update the height of the current node
        node.height = 1 + max(getHeight(node.left), getHeight(node.right));  // Height is 1 plus the height of the taller subtree

        // Step 3: Get the balance factor of the current node to check if it is unbalanced
        int balance = getBalance(node);

        // Step 4: If the node is unbalanced, perform AVL rotations to restore balance

        // Case 1: Left Left Case (Single right rotation needed)
        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        // Case 2: Right Right Case (Single left rotation needed)
        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        // Case 3: Left Right Case (Double rotation: left rotation on left child, then right rotation on current node)
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Case 4: Right Left Case (Double rotation: right rotation on right child, then left rotation on current node)
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        // Step 5: Return the (unchanged) node pointer
        return node; 
    }

    // Utility function to perform right rotation
    Node rightRotate(Node y) {
        Node x = y.left;  // x becomes the new root of the subtree
        Node T2 = x.right;  // T2 is the right subtree of x
        
        // Perform the rotation
        x.right = y;
        y.left = T2;

        // Update heights of the nodes involved in the rotation
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1;

        // Return the new root (x)
        return x;
    }

    // Utility function to perform left rotation
    Node leftRotate(Node x) {
        Node y = x.right;  // y becomes the new root of the subtree
        Node T2 = y.left;  // T2 is the left subtree of y
        
        // Perform the rotation
        y.left = x;
        x.right = T2;

        // Update heights of the nodes involved in the rotation
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1;

        // Return the new root (y)
        return y;
    }
}
