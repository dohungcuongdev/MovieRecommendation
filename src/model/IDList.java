package model;

/////////////////////////////This class is created to create the IDlist for itemsets
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Vector;

public class IDList{
	////the length of it
	private ArrayList<Integer> SID;
//	private ArrayList <ArrayList<Integer>> EID;
	private ArrayList<Integer> EID;
	private ArrayList<Integer> sequence ;
	////////////denote the itemset delimite,it determines the connection-sign before the sequence bit.
	private ArrayList<Boolean>BID;
	////two-dimensional array for storing the eid list
	public IDList(ArrayList<Integer> c){
		sequence=c;
		//EID=new ArrayList<ArrayList<Integer>>();
		EID=new ArrayList<Integer>();
		SID=new ArrayList<Integer>();
		BID=new ArrayList<Boolean>();
		//for(int i=0;i<sequence.size();i++){
			//System.out.println("sequence sieze="+sequence.size());
		//	ArrayList bMap=new ArrayList<Integer>();
		//	EID.add(bMap);
		//}			
	}
	public void set(int sid,int eid){
		SID.add(sid);
		EID.add(eid);
		/////////make sure the BID should be increased once.
		if (BID.size()==0)
			BID.add(true);
	}
	public void sortItemset(){
		PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
		int count=0;
		int start=0;
		boolean incoming=false;
		for(int i=0;i<BID.size();i++){
			if(BID.get(i)==false){
				if(!incoming){
					incoming=true;
					count=1;
					start=i;
					pq.clear();
					}
				else
					count++;	
			pq.add((Integer)this.sequence.get(i));
			}else if(incoming){
				incoming=false;
				if (count==1)
					this.BID.set(start,true);
				else
					for(int j=0;j<count;j++)
						this.sequence.set(start+j, pq.poll());
			}
		}//end of for
		if(incoming)
			if (count==1)
				this.BID.set(start,true);
			else
				for(int j=0;j<count;j++)
					this.sequence.set(start+j, pq.poll());
	}
	public String toString(){
		int length = sequence.size();
		//length=0;
		String tmp="Sequence="+sequence.toString()+" support account="+this.support()+" BID="+BID+"\n"+"SID EID";
	//	for(int i=0;i<length;i++)
	//		tmp+=sequence.get(i)+" ";
		tmp+="\n";
		int i=0;
		while(i<SID.size()){
			tmp+=SID.get(i)+"   "+EID.get(i)+"   ";
		    tmp+="\n";
		    i++;
		}
		return tmp;
		
	}
	///calculate the support of the pattern
	public int support(){
		int support_count=0;
		Vector ItemList=new  Vector<Integer>();
		for(int i=0;i<SID.size();i++){
			if(!ItemList.contains(SID.get(i)))
        	ItemList.add(SID.get(i)); 
		}
		support_count=ItemList.size();
		ItemList=null;
		return support_count;
			
	}
	public IDList join(IDList b,int support){
		if(this.sequence.size()!=b.sequence.size())
			return null;
		//////to deal with sequence atoms
		if(this.sequence.size()>1)
			if(this.BID.get(this.sequence.size()-1)!=true || b.BID.get(this.sequence.size()-1)!=true)
				return null;
		//////
		for(int i=0;i<this.sequence.size()-1;i++)
			if(!this.sequence.get(i).equals(b.sequence.get(i)))
				return null;
		for(int i=0;i<this.sequence.size()-1;i++)
			if(!this.BID.get(i).equals(b.BID.get(i)))
				return null;
		
		ArrayList c=new ArrayList<Integer>();
		for(int i=0;i<this.sequence.size();i++)
			c.add(this.sequence.get(i));
		c.add(b.sequence.get(this.sequence.size()-1));
		IDList tmp=new IDList(c);
		c=null;
		for(int i=0;i<this.SID.size();i++)
			for(int j=0;j<b.SID.size();j++){
				if(!this.SID.get(i).equals(b.SID.get(j)))
					continue;
				if(this.EID.get(i)<b.EID.get(j)){
					    boolean newTmp=true;
						int k;
					    for(k=0;k<tmp.SID.size();k++)   	
					    	if(tmp.SID.get(k).equals(this.SID.get(i)) && tmp.EID.get(k).equals(b.EID.get(j))){
					    		newTmp=false;
					    		break;
					    	}
					    		
					    if (newTmp){
					    	tmp.SID.add(this.SID.get(i));
							tmp.EID.add(b.EID.get(j));
							}
				}
			}
		///delete any sets who has empty support count
		if (tmp.support()<support){
			tmp=null;
			return null;
		}else{
			tmp.BID= (ArrayList<Boolean>) b.BID.clone();
			tmp.BID.add(true);
			return tmp;
		}
			
	
	}
	public IDList joinAtomAtom(IDList b,int support){
		if(this.sequence.size()!=b.sequence.size())
			return null;
		//////to deal with sequence atoms
		if(this.sequence.size()>1){
			if(this.BID.get(this.sequence.size()-1)!=false || b.BID.get(this.sequence.size()-1)!=false)
				return null;
		}
		
		for(int i=0;i<this.sequence.size()-1;i++)
			if(!this.sequence.get(i).equals(b.sequence.get(i)))
				return null;
		for(int i=0;i<this.sequence.size()-1;i++)
			if(!this.BID.get(i).equals(b.BID.get(i)))
				return null;
		ArrayList c=new ArrayList<Integer>();
		for(int i=0;i<this.sequence.size();i++)
			c.add(this.sequence.get(i));
		c.add(b.sequence.get(this.sequence.size()-1));
		IDList tmp=new IDList(c);
		c=null;
		for(int i=0;i<this.SID.size();i++)
			for(int j=0;j<b.SID.size();j++){
				if(!this.SID.get(i).equals(b.SID.get(j)))
					continue;
				if(this.EID.get(i)==b.EID.get(j)){
					    boolean newTmp=true;
						int k;
					    for(k=0;k<tmp.SID.size();k++)   	
					    	if(tmp.SID.get(k).equals(this.SID.get(i)) && tmp.EID.get(k).equals(b.EID.get(j))){
					    		newTmp=false;
					    		break;
					    	}
					    		
					    if (newTmp){
					    	tmp.SID.add(this.SID.get(i));
							tmp.EID.add(b.EID.get(j));
							}
				   
				}
			}
		///delete any sets who has empty support count
		if (tmp.support()<support){
			tmp=null;
			return null;
		}else{
			if(this.sequence.size()>1)
				tmp.BID=new ArrayList<Boolean>(this.BID);
			else
				tmp.BID.add(false);
			tmp.BID.add(false);
			return tmp;
		}
			
	
	}
	public IDList joinAtomSeq(IDList b,int support){
		if(this.sequence.size()!=b.sequence.size())
			return null;
		//////to deal with sequence atoms
	     /////if the atom is one-length, it should be includes in the sequence join sequence
			if(this.BID.get(this.sequence.size()-1)!=false || b.BID.get(this.sequence.size()-1)!=true)
				return null;
	    
		for(int i=0;i<this.sequence.size()-1;i++)
			if(!this.sequence.get(i).equals(b.sequence.get(i)))
				return null;
		//////deal with bid with single true on the size()-2 point
		if(this.sequence.size()>1){
			if(this.BID.get(this.sequence.size()-2) || b.BID.get(b.sequence.size()-2)){
				for(int i=0;i<this.sequence.size()-2;i++)
					if(!this.BID.get(i).equals(b.BID.get(i)))
						return null;
			}else
				for(int i=0;i<this.sequence.size()-1;i++)
					if(!this.BID.get(i).equals(b.BID.get(i)))
						return null;
		}
			 
		ArrayList c=new ArrayList<Integer>();
		for(int i=0;i<this.sequence.size();i++)
			c.add(this.sequence.get(i));
		c.add(b.sequence.get(this.sequence.size()-1));
		IDList tmp=new IDList(c);
		c=null;
		for(int i=0;i<this.SID.size();i++)
			for(int j=0;j<b.SID.size();j++){
				if(!this.SID.get(i).equals(b.SID.get(j)))
					continue;
				if(this.EID.get(i)<b.EID.get(j)){
					    boolean newTmp=true;
						int k;
					    for(k=0;k<tmp.SID.size();k++)   	
					    	if(tmp.SID.get(k).equals(this.SID.get(i)) && tmp.EID.get(k).equals(b.EID.get(j))){
					    		newTmp=false;
					    		break;
					    	}
					    		
					    if (newTmp){
					    	tmp.SID.add(this.SID.get(i));
							tmp.EID.add(b.EID.get(j));
							}
				}
			}
		///delete any sets who has empty support count
		if (tmp.support()<support){
			tmp=null;
			return null;
		}else{
			tmp.BID=new ArrayList<Boolean>(this.BID);
			tmp.BID.add(true);
			return tmp;
		}
			
	
	}
	public IDList joinAF(IDList b,int support){
		////////////l-length should be included in atomJoinatom already 
		if(this.sequence.size()!=b.sequence.size() || this.sequence.size()==1)
			return null;

		if(this.BID.get(this.sequence.size()-1)!=true || b.BID.get(this.sequence.size()-1)!=true)
				return null;

		for(int i=0;i<this.sequence.size()-1;i++)
			if(!this.sequence.get(i).equals(b.sequence.get(i)))
				return null;
		for(int i=0;i<this.sequence.size()-1;i++)
			if(!this.BID.get(i).equals(b.BID.get(i)))
				return null;
		ArrayList c=new ArrayList<Integer>();
		for(int i=0;i<this.sequence.size();i++)
			c.add(this.sequence.get(i));
		c.add(b.sequence.get(this.sequence.size()-1));
		IDList tmp=new IDList(c);
		c=null;
		for(int i=0;i<this.SID.size();i++)
			for(int j=0;j<b.SID.size();j++){
				if(!this.SID.get(i).equals(b.SID.get(j)))
					continue;
				if(this.EID.get(i)==b.EID.get(j)){
					    boolean newTmp=true;
						int k;
					    for(k=0;k<tmp.SID.size();k++)   	
					    	if(tmp.SID.get(k).equals(this.SID.get(i)) && tmp.EID.get(k).equals(b.EID.get(j))){
					    		newTmp=false;
					    		break;
					    	}
					    		
					    if (newTmp){
					    	tmp.SID.add(this.SID.get(i));
							tmp.EID.add(b.EID.get(j));
							}	
				}
			}
		///delete any sets who has empty support count
		if (tmp.support()<support){
			tmp=null;
			return null;
		}else{
			for(int i=0;i<b.BID.size()-1;i++)
				tmp.BID.add(b.BID.get(i));
			tmp.BID.add(false);
			tmp.BID.add(false);
			return tmp;
		}
			
	
	}
	public String getSequence() {
		String temp="[";
		boolean comein=false;
		for(int i=0;i<sequence.size();i++){
			//System.out.println("Note"+this.sequence);
			if(this.BID.get(i)==false){
			      if(comein==false){
			    	  comein=true;
			    	  temp+="("+sequence.get(i);
			      }else
			    	  temp+=","+sequence.get(i);
			}else{
				if(comein==true){
					comein=false;
					temp+=") "+sequence.get(i);
					}
				else
					temp+=" "+sequence.get(i);
				
			}
		}
	    ///////////last one for close the bracket
        if(comein==true && !this.BID.get(sequence.size()-1))
        	temp+=")";
		temp+="]";
		return temp;
	}
	
	public String getFirstSequence() {
		String temp="";
		for(int i=0;i<sequence.size();i++){
			temp+=sequence.get(i);
		}
		return temp;
	}
	
	public String getLastSequence() {
		String temp="";
		boolean comein=false;
		for(int i=0;i<sequence.size();i++){
			//System.out.println("Note"+this.sequence);
			if(this.BID.get(i)==false){
			      if(comein==false){
			    	  comein=true;
			    	  temp+=","+sequence.get(i);
			      }else
			    	  temp+=","+sequence.get(i);
			}else{
				if(comein==true){
					comein=false;
					temp+=","+sequence.get(i);
					}
				else
					temp+=" "+sequence.get(i);
				
			}
		}
		return temp.replace(" ", ",").substring(1);
	}
	
	public void setSequence(ArrayList<Integer> sequence) {
		this.sequence = sequence;
	}


    public static class CompratorBySupport implements Comparator {

       public int compare(Object o1, Object o2) {

           int seq1 = ((IDList)o1).support();

           int seq2 = ((IDList)o2).support();

           long diff = seq2 - seq1;

           if (diff > 0)

              return 1;

           else if (diff == 0)

              return 0;

           else

              return -1;

       }
    
       

       public boolean equals(Object obj){

           return true;  

       } 
    }

}
