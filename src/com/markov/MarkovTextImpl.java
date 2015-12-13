package com.markov;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MarkovTextImpl implements MarkovText{



	// The list of words with their next words
	private List<ListNode> wordList;

	// The starting "word"
	private String starter;

	// The random number generator
	private Random rnGenerator;

	public boolean search(String text) {

		for (int i = 0; i < wordList.size(); i++) {

			if (wordList.get(i).getWord().equals(text)) {

				return true;
			}
		}

		return false;
	}

	public int searchPos(String text) {

		for (int i = 0; i < wordList.size(); i++) {

			if (wordList.get(i).getWord().equals(text)) {
				System.out.println("Word found--->");
				return i;
			}
		}

		return -1;
	}

	public int indexOf(String text) {

		for (int i = 0; i < wordList.size(); i++) {

			if (wordList.get(i).getWord().equals(text))
				return i;

		}

		return -1;
	}

	protected List<String> getTokens(String pattern, String text) {
		ArrayList<String> tokens = new ArrayList<String>();
		Pattern tokSplitter = Pattern.compile(pattern);
		Matcher m = tokSplitter.matcher(text);

		while (m.find()) {
			tokens.add(m.group());
		}

		return tokens;
	}

	public MarkovTextImpl(Random generator) {
		wordList = new LinkedList<ListNode>();
		starter = "";
		rnGenerator = generator;
	}

	/** Train the generator by adding the sourceText */

	@Override
	public void train(String sourceText) {
		if(!sourceText.equals("")){



		String prevWord;

		List<String> words = getTokens("[a-zA-Z]+", sourceText);


		starter = words.get(0);
		prevWord = starter;

		for (int i = 1; i <= words.size(); i++) {

			if (search(prevWord)) {


				if (i == words.size())
					break;
				else
					wordList.get(indexOf(prevWord)).addNextWord(words.get(i));

			} else {

				wordList.add(new ListNode(prevWord));
				if (i == words.size())
					break;
				else
					wordList.get(indexOf(prevWord)).addNextWord(words.get(i));
			}
			prevWord = words.get(i);
		}

		wordList.get(indexOf(prevWord)).addNextWord(starter);

		// TODO: Implement this method
		}
	}

	/**
	 * Generate the number of words requested.
	 */

	@Override
	public String generateText(int numWords) {
		// TODO: Implement this method
		if(wordList.size()<=0)return  null;
		String currWord = starter;
		System.out.println("currWord-->" + currWord);
		String output = " ";
		output += currWord + " ";
		int pos = 0;
		for (int i = 0; i < numWords; i++) {
			System.out.println(i + "th iteration");
			pos = searchPos(currWord);
			System.out.println("pos--->" + pos);
			String randomWord = wordList.get(pos).getRandomNextWord(rnGenerator);
			output += randomWord + " ";
			currWord = randomWord;

		}

		return output;
	}

	// Can be helpful for debugging
	@Override
	public String toString() {
		String toReturn = "";
		for (ListNode n : wordList) {
			toReturn += n.toString();
		}
		return toReturn;
	}

	/** Retrain the generator from scratch on the source text */
	@Override
	public void retrain(String sourceText) {
		// TODO: Implement this method.

		wordList.clear();

		String prevWord;
		if(!sourceText.equals("")){

		List<String> words = getTokens("[a-zA-Z]+", sourceText);

		/*
		 * for (int i = 1; i < words.size(); i++){ System.out.println(i +
		 * "th element = " + words.get(i)); }
		 */
		starter = words.get(0);
		prevWord = starter;

		for (int i = 1; i <= words.size(); i++) {

			if (search(prevWord)) {

				// System.out.println("inside if--->");
				if (i == words.size())
					break;
				else
					wordList.get(indexOf(prevWord)).addNextWord(words.get(i));

			} else {

				wordList.add(new ListNode(prevWord));
				if (i == words.size())
					break;
				else
					wordList.get(indexOf(prevWord)).addNextWord(words.get(i));
			}
			prevWord = words.get(i);
		}

		wordList.get(indexOf(prevWord)).addNextWord(starter);

		}


	}




	public static void main(String[] args) {
		// feed the generator a fixed random value for repeatable behavior
		MarkovTextImpl gen = new MarkovTextImpl(new Random(42));
		String textString = "Hello.  Hello there.  This is a test.  Hello there.  Hello Bob.  Test again.";

		System.out.println(textString);
		gen.train(textString);
		System.out.println("gen.toString() =" + gen.toString());


		System.out.println(gen.generateText(20));

		String textString3 = "OH there is blessing in this gentle breeze," +
         " A visitant that while it fans my cheek"+
          "Doth seem half-conscious of the joy it brings"+
          "From the green fields, and from yon azure sky."+
          "Whate'er its mission, the soft breeze can come"+
          "To none more grateful than to me; escaped"+
          "From the vast city, where I long had pined"+
          "A discontented sojourner: now free,"+
          "Free as a bird to settle where I will."+
          "What dwelling shall receive me? in what vale"+
          "Shall be my harbour? underneath what grove"+
          "Shall I take up my home? and what clear stream"+
          "Shall with its murmur lull me into rest?"+
          "The earth is all before me. With a heart"+
          "Joyous, nor scared at its own liberty,"+
          "I look about; and should the chosen guide"+
          "Be nothing better than a wandering cloud,"+
          "I cannot miss my way. I breathe again!"+
          "Trances of thought and mountings of the mind"+
          "Come fast upon me: it is shaken off,"+
          "That burthen of my own unnatural self,"+
          "The heavy weight of many a weary day"+
          "Not mine, and such as were not made for me."+
          "Long months of peace (if such bold word accord"+
          "With any promises of human life),"+
          "Long months of ease and undisturbed delight"+
          "Are mine in prospect; whither shall I turn,"+
          "By road or pathway, or through trackless field,"+
          "Up hill or down, or shall some floating thing"+
          "Upon the river point me out my course?   ";


		  gen.retrain(textString3);
		  System.out.println(gen);
		  System.out.println(gen.generateText(20));

	}



}
