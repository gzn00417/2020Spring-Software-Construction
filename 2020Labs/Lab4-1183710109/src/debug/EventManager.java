package debug;

import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * 实现一个EventManager类来管理个人日程，通过该类的一个方法
 * 
 * book(int day, int start, int end)
 * 
 * 来添加新事件
 * 
 * 待添加的新事件发生在day，这是一个整数，表示一年里的第day天
 * 
 * start表示事件的起始时间，为该day天的第start小时
 * 
 * end表示该事件的结束时间，为该day天的第end小时。
 * 
 * 例如：
 * book(1,8,10)表示添加一个在1月1日（第1天）的8点开始，10点结束的事件。
 * book(1, 0, 1)表示在第1天的0:00-1:00的事件  
 * book(1, 22,24)表示在第1天的22:00-24:00的事件
 * 
 * 事件的长度单位是小时，不需要考虑分钟。
 * 
 * 约束条件：1<=day<=365（无需考虑闰年之类的问题），0<=start<end<=24。

 * “k-重叠”是指：有k个事件的时间范围在某个时间段内存在交集，即这k个事件在某个小时内都已经启动且尚未结束。
 * book(…)方法的返回值是：当本次调用结束后的最大k值。
 * 
 * 例如：
 * 
 * EventManager.book(1, 10, 20); 	// returns 1
 * EventManager.book(1, 1, 7); 		// returns 1
 * EventManager.book(1, 10, 22); 	// returns 2
 * EventManager.book(1, 5, 15); 	// returns 3
 * EventManager.book(1, 5, 12); 	// returns 4
 * EventManager.book(1, 7, 10); 	// returns 4
 * 
 * 请对以下代码进行调试和修改，使其完整、正确、健壮的完成上述需求，但不要改变该代码的内在逻辑。
 *
 * 
 */
public class EventManager {

	static Map<Integer, Integer> temp = new TreeMap<>();
	static int GAP = 25;

	/**
	 * 
	 * @param start start time of the event to be added, should be in [0, 24)
	 * @param end   end time of the event to be added, should be in (0, 24]
	 * @return 		the max number of concurrent events in the same hour
	 */
	public static int book(int day, int start, int end) {
		if ((day >= 1 && day <= 365 && start >= 0 && start < 24 && end > 0 && end <= 24 && start < end)) {
			if (temp.containsKey(start + day * GAP))
				temp.put(start + day * GAP, temp.get(start + day * GAP) + 1);
			else
				temp.put(start + day * GAP, 1);
			if (temp.containsKey(end + day * GAP))
				temp.put(end + day * GAP, temp.get(end + day * GAP) - 1);
			else
				temp.put(end + day * GAP, -1);
		}
		int active = 0, ans = 0;
		for (int d : temp.values()) {
			active += d;
			if (active > ans)
				ans = active;
		}
		return ans;
	}

}
