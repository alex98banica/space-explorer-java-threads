import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class that implements the channel used by headquarters and space explorers to communicate.
 */
public class CommunicationChannel {

	/**
	 * Creates a {@code CommunicationChannel} object.
	 */

	ConcurrentLinkedQueue<Message> spaceExplorer = new ConcurrentLinkedQueue<Message>();
   	ArrayBlockingQueue<Message> headQuarters = new ArrayBlockingQueue<Message>(1000);
	ReentrantLock mutex = new ReentrantLock();
	ReentrantLock mutex2 = new ReentrantLock();
	int t=0;

	public CommunicationChannel() {
	}

	/**
	 * Puts a message on the space explorer channel (i.e., where space explorers write to and 
	 * headquarters read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageSpaceExplorerChannel(Message message) {
		try{
			spaceExplorer.add(message);
			mutex2.unlock();
		}
		catch(Exception e){
		}
	}

	/**
	 * Gets a message from the space explorer channel (i.e., where space explorers write to and
	 * headquarters read from).
	 * 
	 * @return message from the space explorer channel
	 */
	public Message getMessageSpaceExplorerChannel() {
		try {
			mutex2.lock();
			Message m1= spaceExplorer.poll();
			return m1;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Puts a message on the headquarters channel (i.e., where headquarters write to and 
	 * space explorers read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageHeadQuarterChannel(Message message) {
		if(t%2==0)
		{
			try{
				mutex.lock();
				headQuarters.put(message);
				t++;
			}
			catch(Exception e){}
		}
		else
		{
			try{
				headQuarters.put(message);
				mutex.unlock();
				t--;
			}
			catch(Exception e)
			{
			}
		}
	}

	/**
	 * Gets a message from the headquarters channel (i.e., where headquarters write to and
	 * space explorer read from).
	 * 
	 * @return message from the header quarter channel
	 */
	public Message getMessageHeadQuarterChannel()  {
        try {
            Message m1= headQuarters.take();
        	return m1;
        }
        catch (Exception e) {
        }
		return null;
	}
}
