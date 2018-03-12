package service;

import db.MySQLConnector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import model.Frequentpattern;
import model.IDList;
import model.Input;
import model.ItemSet;
import model.Sequence;

public class SpadeAlgorithm {
	
	int SequenceMAXNumber = 1945;

	public static int getAllCID() {

		MySQLConnector m = new MySQLConnector();
		try {
			String sql = "SELECT count(DISTINCT cid) FROM rating";
			PreparedStatement statement = m.connect().prepareStatement(sql);
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			m.disconnect();
		}
		return -1;
	}

	public ArrayList getFreSeqList(double support, int length) {
		int SequenceMAXNumber = this.SequenceMAXNumber;
		String filename = "src/dataset/dataset.txt";
		String path = "/Users/nguyenminh/NetBeansProjects/Spade/src/src/Main/result.txt";
		int support_account;
		///// denote the max 2-k-bit length of sequence
		int bitLength = 0;

		Sequence Seq[] = new Sequence[SequenceMAXNumber];
		support_account = (int) Math.ceil(SequenceMAXNumber * support);
		Input input = new Input();
		input.setProperty(filename);
		for (int i = 0; i < SequenceMAXNumber; i++) {
			// System.out.println (i);
			Sequence tmp = new Sequence(input.readLine(), i + 1, input.getClasslabel());
			Seq[i] = tmp;
			// tmp.test();
		}
		/// generate the bitmap representation of the dataset
		Vector ItemList = new Vector<Integer>();
		for (int i = 0; i < SequenceMAXNumber; i++) {
			if (Seq[i].size() > bitLength) {
				bitLength = Seq[i].size();
			}
			for (int j = 0; j < Seq[i].size(); j++) {
				for (int k = 0; k < Seq[i].get(j).size(); k++) {
					if (!ItemList.contains(Seq[i].get(j).get(k))) {
						ItemList.add(Seq[i].get(j).get(k));
					}
				}
			}

		}
//		System.out.println("\n\n\n\n\n\n");
//		for (int i = 0; i < SequenceMAXNumber; i++) {
//			System.out.println("\nSequence " + (i + 1) + ": " + Seq[i]);
//		}
//		for (int i = 0; i < ItemList.size(); i++) {
//			System.out.println(i + "---" + ItemList.get(i));
//		}
		//
		bitLength = GenerateLog2(bitLength);
		HashMap SeqList = new HashMap<ArrayList<Integer>, IDList>();

		for (int i = 0; i < ItemList.size(); i++) {
			ArrayList tmp = new ArrayList<Integer>();
			tmp.add(ItemList.get(i));
			// System.out.println(tmp);
			/// tmp is the itemset,start from a,b,c,...
			SeqList.put(tmp, new IDList(tmp));
		}
		//// initialize 1-sequence IDlist
		for (int i = 0; i < SequenceMAXNumber; i++) {
			for (int j = 0; j < Seq[i].size(); j++) {
				ItemSet s = Seq[i].get(j);
				for (int k = 0; k < Seq[i].get(j).size(); k++) {
					ArrayList tmp = new ArrayList<Integer>();
					tmp.add(s.get(k));// j is the EID
					// System.out.println("sssssssssssssssss"+tmp+"i->"+i+"j-->"+j+"!!!");
					((IDList) SeqList.get(tmp)).set(i, j);
				}

			}
		}
		ArrayList FreSeqList = new ArrayList<ArrayList>();
		ArrayList FreSeqListTmp = new ArrayList<IDList>();
		Iterator iterator = SeqList.keySet().iterator();
		while (iterator.hasNext()) {
			IDList value = (IDList) SeqList.get(iterator.next());
			if (value.support() >= support_account) {
				FreSeqListTmp.add(value);
			}
			/// check 1-length idlist
			/// System.out.println(value.toString());
		}
		Collections.sort(FreSeqListTmp, new IDList.CompratorBySupport());
		FreSeqList.add(FreSeqListTmp);

		System.out.println("System is analysing the data,...");
		/// create the from sequence 1 to sequece length
		length = length - 1;/// the actual maximium length is Length+1
		for (int i = 0; i < length; i++) {
			ArrayList f = (ArrayList) FreSeqList.get(i);

			ArrayList Tmp = new ArrayList<IDList>();
			for (int j = 0; j < f.size(); j++) {
				for (int k = j; k < f.size(); k++) {
					if (k == j) {
						IDList value = ((IDList) f.get(j)).join((IDList) f.get(k), support_account);
						if (null != value) {
							Tmp.add(value);
						}
					} else {
						IDList value = ((IDList) f.get(j)).join((IDList) f.get(k), support_account);
						if (null != value) {
							Tmp.add(value);
						}
						value = ((IDList) f.get(k)).join((IDList) f.get(j), support_account);
						if (null != value) {
							Tmp.add(value);
						}
						value = ((IDList) f.get(j)).joinAF((IDList) f.get(k), support_account);
						if (null != value) {
							Tmp.add(value);
						}
						value = ((IDList) f.get(j)).joinAtomAtom((IDList) f.get(k), support_account);
						if (null != value) {
							Tmp.add(value);
						}
						value = ((IDList) f.get(j)).joinAtomSeq((IDList) f.get(k), support_account);
						if (null != value) {
							Tmp.add(value);
						}
						value = ((IDList) f.get(k)).joinAtomSeq((IDList) f.get(j), support_account);
						if (null != value) {
							Tmp.add(value);
						}
					}
				} // end of for int k=j
			}
			for (int index = 0; index < Tmp.size(); index++) {
				((IDList) Tmp.get(index)).sortItemset();
			}
			Collections.sort(Tmp, new IDList.CompratorBySupport());
			FreSeqList.add(Tmp);
		}
		return FreSeqList;
	}

	public String getResultAlgorithm(double support, int length) {
		return getResult(getFreSeqList(support, length));
	}

	public List<Frequentpattern> getFirstfrequentpatterns(double support, int length) {
		return getFirstfrequentpatterns(getFreSeqList(support, length));
	}

	private int GenerateLog2(int length) {
		double tmp = Math.log((double) length) / Math.log(2.0);
		if (Math.pow(2, Math.ceil(tmp)) < 4) {
			return 4;
		}
		return (int) Math.pow(2, Math.ceil(tmp));
	}

	public String getResult(ArrayList Freq) {
		IDList value;
		int support;
		String str = "";
		for (int length = 0; length < Freq.size(); length++) {
			str += "The " + (length + 1) + "-frequent patterns is: \n ";
			ArrayList tmp = (ArrayList) Freq.get(length);
			for (int i = 0; i < tmp.size(); i++) {
				value = (IDList) tmp.get(i);
				support = value.support();
				str += value.getSequence() + "(support=" + support + ")\n ";
			}
		}
		return str;
	}

	public List<Frequentpattern> getFirstfrequentpatterns(ArrayList Freq) {
		IDList value;
		int support;
		List<Frequentpattern> frequentPattern = new ArrayList<>();
		List<IDList> tmp = (ArrayList) Freq.get(0);
		for (int i = 0; i < tmp.size(); i++) {
			value = (IDList) tmp.get(i);
			support = value.support();
			Frequentpattern fp = new Frequentpattern(value.getSequence(), support);
			frequentPattern.add(fp);
		}
		return frequentPattern;
	}

	public List<Integer> getFirstfrequentpatternsMovieID(ArrayList Freq) {
		IDList value;
		int support;
		List<Integer> frequentPattern = new ArrayList<>();
		List<IDList> tmp = (ArrayList) Freq.get(0);
		for (int i = 0; i < tmp.size(); i++) {
			value = (IDList) tmp.get(i);
			int movieID = Integer.parseInt(value.getFirstSequence());
			frequentPattern.add(movieID);
		}
		return frequentPattern;
	}
	
	public List<Integer> getFrequentpatternsMovieID(ArrayList Freq, String mid) {
		IDList value;
		int support;
		List<Integer> frequentPattern = new ArrayList<>();
		List<IDList> tmp = (ArrayList) Freq.get(2 - 1);
		for (int i = 0; i < tmp.size(); i++) {
			value = (IDList) tmp.get(i);
			String mids[] = value.getLastSequence().split(",");
			List<String> lMids = Arrays.asList(mids);
			if(lMids.contains(mid)) {
				for(String midS: lMids) {
					int midI = Integer.parseInt(midS);
					//if(!midS.equals(mid))
					if(!midS.equals(mid) && !frequentPattern.contains(midI))
						frequentPattern.add(midI);
				}
			}
		}
		return frequentPattern;
	}

	public static void main(String args[]) {
		SpadeAlgorithm s = new SpadeAlgorithm();
		System.out.println("Firstfrequentpattern: " + s.getFirstfrequentpatterns(0.3, 1));

	}

}
