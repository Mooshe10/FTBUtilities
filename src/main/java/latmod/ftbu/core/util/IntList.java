package latmod.ftbu.core.util;

import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.*;

public class IntList implements Iterable<Integer>// Improve this // FastList
{
	private final int init;
	private int defVal = -1;
	private int array[];
	private int size;
	
	public IntList(int i)
	{ init = i; array = new int[init]; }
	
	public IntList()
	{ this(0); }
	
	public int size()
	{ return size; }
	
	public void clear()
	{ size = 0; array = new int[init]; }
	
	public void setDefVal(int value)
	{ defVal = value; }
	
	public void add(int value)
	{
		int array1[] = new int[size + 1];
		for(int i = 0; i < size; i++)
			array1[i] = array[i];
		array1[array1.length - 1] = value;
		array = array1;
		size++;
	}
	
	public void addAll(int... values)
	{
		if(values.length <= 0) return;
		int array1[] = new int[size + values.length];
		for(int i = 0; i < size; i++)
			array1[i] = array[i];
		for(int i = 0; i < values.length; i++)
			array1[array1.length - values.length + i] = values[i];
		array = array1;
		size += values.length;
	}
	
	public void addAll(IntList l)
	{ if(l.size > 0) addAll(l.toArray()); }
	
	public void setAll(int... values)
	{ clear(); addAll(values); }
	
	public int get(int index)
	{ return (index >= 0 && index < size()) ? array[index] : defVal; }
	
	public int indexOf(int value)
	{
		for(int i = 0; i < size(); i++)
			if(array[i] == value) return i;
		return -1;
	}
	
	public boolean contains(int value)
	{ return indexOf(value) != -1; }
	
	public int removeKey(int key)
	{
		if(key < 0 || key >= size) return defVal;
		int rem = get(key);
		size--;
		for(int j = key; j < size; j++)
		array[j] = array[j + 1];
		return rem;
	}
	
	public int removeValue(int value)
	{ return removeKey(indexOf(value)); }
	
	public void set(int i, int value)
	{ array[i] = value; }
	
	public boolean isEmpty()
	{ return size <= 0; }
	
	public int[] toArray()
	{
		if(size <= 0) return new int[0];
		int ai[] = new int[size];
		for(int i = 0; i < size; i++)
			ai[i] = array[i];
		return ai;
	}
	
	public void sort()
	{
		int[] a = toArray();
		Arrays.sort(a);
		clear();
		addAll(a);
	}

	public IntList copy()
	{
		IntList l = new IntList(init);
		l.defVal = defVal;
		l.array = toArray();
		l.size = size;
		return l;
	}
	
	public int hashCode()
	{
		int h = 0;
		for(int i = 0; i < size; i++)
			h = h * 31 + array[i];
		return h;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		
		for(int i = 0; i < size; i++)
		{
			sb.append(array[i]);
			if(i != size - 1)
				sb.append(", ");
		}
		
		sb.append(" ]");
		return sb.toString();
	}
	
	public Iterator<Integer> iterator()
	{ return new IntIterator(array); }
	
	public IntList clone()
	{
		IntList l = new IntList(init);
		l.array = array.clone();
		return l;
	}
	
	public static class IntIterator implements Iterator<Integer>
	{
		public final int[] values;
		public int pos = -1;
		
		public IntIterator(int[] v)
		{ values = v; }
		
		public boolean hasNext()
		{ return pos < values.length; }
		
		public Integer next()
		{ return Integer.valueOf(values[++pos]); }
	}
	
	public static class Serializer implements JsonDeserializer<IntList>, JsonSerializer<IntList>
	{
		public JsonElement serialize(IntList src, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonArray o = new JsonArray();
			for(int i = 0; i < src.size; i++)
				o.add(new JsonPrimitive(src.array[i]));
			return o;
		}
		
		public IntList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			if(json.isJsonNull()) return null;
			
			IntList list = new IntList();
			JsonArray o = json.getAsJsonArray();
			for(int i = 0; i < o.size(); i++)
				list.add(o.get(i).getAsInt());
			return list;
		}
	}
}