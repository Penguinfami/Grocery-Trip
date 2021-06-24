/** SimplePriorityQueueNode.java
 * @author Christine Zhao
 * @param <E> The item class
 * Nodes used for priority queues
 */
public class SimplePriorityQueue<E> {

    //head
    private PriorityQueueNode<E> head;
    //tail
    private PriorityQueueNode<E> tail;

    /**
     * Add an item to the queue, uses recursion to find the correct place in the queue
     * @param item The object to add
     * @param priority The priority of the item
     */
    public void enqueue(E item, int priority){
        if (head == null){ // if there are no items in the queue
            head = new PriorityQueueNode<E> (item, priority, null, null);
            tail = head;
            return;
        }
        if (head.getPriority() < priority){ // if the new item should become the new head
            head = new PriorityQueueNode<E> (item, priority, head, null);
            return;
        }
        // recursive method to place in queue
        enqueue2(item, priority, head, head.getNext());
    }

    private void enqueue2(E item, int priority, PriorityQueueNode<E> prevNode, PriorityQueueNode<E> nextNode){
        if (nextNode == null){
            prevNode.setNext(new PriorityQueueNode<E>(item, priority,null, prevNode));
            tail = prevNode.getNext();
            return;
        } else if (nextNode.getPriority() < priority){
            prevNode.setNext(new PriorityQueueNode<E>(item, priority, nextNode, prevNode));
            nextNode.setPrevious(prevNode.getNext());
            return;
        } else {
            enqueue2(item, priority, prevNode.getNext(), nextNode.getNext());
        }
    }

    //Dequeue

    /**
     * Remove the first item from the queue
     * @return The item removed
     */
    public PriorityQueueNode<E> dequeue(){
        PriorityQueueNode<E> previousHead = head;
        if (head != null){
            head = head.getNext();
        }
        return previousHead;
    }



}

