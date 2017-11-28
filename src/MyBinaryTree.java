import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class MyBinaryTree<E extends Comparable<E>> implements Iterable<E>{
	private MyTreeNode<E> root, mover;
	boolean recordfind=false;
	public MyBinaryTree(){
		root = null;
		
	}
	
	public boolean isEmpty(){
		return root == null;
	}
	
	
	public void add(E e){
		if(isEmpty()){
			root = new MyTreeNode<E>(e);
		}else{
			mover = root;
			while(true){
				if(mover.value.compareTo(e)>=0){
					if(mover.nextLeft!=null){
						mover = mover.nextLeft;		
					}else {
						mover.nextLeft = new MyTreeNode<E>(e);
						break;
					}
							
				}else if(mover.value.compareTo(e)<0){
					if(mover.nextRight!=null){
					mover = mover.nextRight;
					}else {
						mover.nextRight = new MyTreeNode<E>(e);
						break;
					}
				}
			}
		}
		
	}
	
	public E search(E e){
		
		
		int compareCount = 0;
		if(isEmpty()) return null;
		
		mover = root;
		while(true){
			if(mover.value.compareTo(e)==0){
				compareCount++;
				return mover.value;
				/*while(mover.nextLeft!=null &&mover.nextLeft.value.compareTo(e)==0){
					compareCount++;
					mover = mover.nextLeft;
					result.add(mover.value);
				}*/
				//System.out.println("Compared times: "+compareCount);
				
			}
			if(mover.value.compareTo(e)>0) {
				compareCount++;
				if(mover.nextLeft!=null) {
					mover=mover.nextLeft;
					continue;
				}
				
				else {
					System.out.println("Compared times: "+compareCount);
					return null;
				}
			}
			
			
			if(mover.value.compareTo(e)<0) 
				compareCount++;
				if(mover.nextRight!=null) {
					mover=mover.nextRight;
					continue;
				}
				else {
					System.out.println("Compared times: "+compareCount);
					return null;
				}
		}
				
	}
	
	public void remove(E e, PrintWriter out){
		recordfind=false;
		root = deleteRec(root, e);
		if(recordfind) out.println(ClientHandler.WELCOMEWORD+"delete successfully");
		else out.println(ClientHandler.WELCOMEWORD+"no records found");
	}
	
	public MyTreeNode<E> deleteRec(MyTreeNode<E> root, E e){
		
		if(root == null) return root;
		
		if(root.value.compareTo(e)>0) root.nextLeft = deleteRec(root.nextLeft,e);
		else if(root.value.compareTo(e)<0) root.nextRight = deleteRec(root.nextRight,e);
		else{
			// node with only one child or no child
			if(root.nextLeft==null) {
				recordfind=true;
				return root.nextRight;
			}
			if(root.nextRight==null) {
				recordfind=true;
				return root.nextLeft;
			}
			
			// node with two children: Get the inorder successor (smallest
            // in the right subtree)
			root.value = minValue(root.nextRight);
			// Delete the inorder successor
            root.nextRight = deleteRec(root.nextRight, root.value);			
		}

		return root;
	}
	
	E minValue(MyTreeNode root)
    {
        E minv = (E)root.value;
        while (root.nextLeft != null)
        {
            minv = (E)root.nextLeft.value;
            root = root.nextLeft;
        }
        return minv;
    }
	
	
	public void update(E olde,E newe, PrintWriter out){
		recordfind=false;
		root = deleteRec(root, olde);
		this.add(newe);
		if(recordfind) out.println(ClientHandler.WELCOMEWORD+"update successfully");
		else out.println(ClientHandler.WELCOMEWORD+"no records found");
	}
	
	
	
	

	public String toString(){
		StringBuilder strBuilder = new StringBuilder();
		Queue<MyTreeNode<E>> queue = new LinkedList<MyTreeNode<E>>();
		MyTreeNode<E> t;
		queue.add(root);
		while(queue.size()!=0){
			t= queue.poll();
			strBuilder.append(t + "\n");
			if(t.nextLeft!=null) queue.add(t.nextLeft);
			if(t.nextRight!=null)queue.add(t.nextRight);
			}
		return strBuilder.toString();
	}

	@Override
	public Iterator<E> iterator() {
		mover = root;
		Queue<MyTreeNode<E>> queue = new LinkedList<MyTreeNode<E>>();
		queue.add(mover);
		
		Iterator<E> itr = new Iterator<E>(){

			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return queue.size()>0;
			}

			@Override
			public E next() {
				// TODO Auto-generated method stub
				mover = queue.poll();
				E tempValue = mover.value;
				if(mover.nextLeft!=null) queue.add(mover.nextLeft);
				if(mover.nextRight!=null)queue.add(mover.nextRight);
				
				return tempValue;
			}
			
		};		
		// TODO Auto-generated method stub
		return itr;
	}

}




class MyTreeNode <E> {
	E value;   
    MyTreeNode<E> nextLeft, nextRight; 
    
    MyTreeNode(E val, MyTreeNode<E> left, MyTreeNode<E> right)
    {
        value = val;
        nextLeft = left;
        nextRight = right;
        
    } 
    
    MyTreeNode(E val)
    {
       // Call the other (sister) constructor.
       this(val, null, null);            
    }
    
    public String toString() {
    	return value.toString();
    }
}