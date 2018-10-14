package com.seedcorn.sentimentAnalysis;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * This class takes an array of reviewId's and reviewText (consecutively) through the main args
 * and runs it through the sentiment analyser.
 * Where necessary, the analysis breaks the reviewText into shorter sentences and scores each one.
 * An multidimensional array is returned consisting of an array (all reviews) of arrays (single review).
 * Each single review is made up of the id first, then a sentence and score as many times as is returned from the anlaysis.
 * If devMode is set to TRUE, the detailed output is displayed in the console.
 */
public class SeedcornSentimentAnalysis {

    private static Object[] findSentiment(String[] allReviews) {
        // Set to TRUE to display the breakdown of the output to the console
        boolean devMode = false;

        if (devMode) {
            System.out.println("" +
                    "\n==============================================================================================================\n" +
                    "The output shows the sentiment analysis for the comment provided.\n" +
                    "The StanfordCoreNlp algorithm breaks apart the input and rates each section numerically as follows:\n" +
                    "\n" +
                    "\t1 = Negative\n" +
                    "\t2 = Neutral\n" +
                    "\t3 = Positive\n" +
                    "\n" +
                    "The returned value is the average of all the sentiment ratings for the provided comment.\n" +
                    "==============================================================================================================\n"
            );
        }

        Object[] arrayToReturn = new Object[allReviews.length / 2];
        ArrayList<String> review = null;
        double totalSentimentScore = 0;
        double numberOfSentences = 0;
        double averageSentimentScore;
        int counter = -1;
        int loopCounter = 0;

        //Setup Stanford Core NLP library
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Loop through the incoming 'allReviews' array.
        // If the loop counter value is odd:
        // - create a reviews ArrayList and add the string (this will be the review Id).
        // If the loop counter value is even:
        // - run the sentiment analysis on the current string,
        // - add the results to the review array,
        // - when the analysis has finished, add the review array to the arrayToReturn.
        for (String reviewText : allReviews) {
            loopCounter++;

            if (loopCounter == 1) {
                review = new ArrayList<>();
                review.add(reviewText);
            } else if (loopCounter == 2) {
                counter++;
                loopCounter = 0;

                if (reviewText != null && reviewText.length() > 0) {
                    Annotation annotation = pipeline.process(reviewText);

                    // Analyse the review, and for each sentence found, run the sentiment analysis.
                    // Add the sentence to the review array.
                    for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                        Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                        int sentenceSentimentScore = RNNCoreAnnotations.getPredictedClass(tree);

                        // Increment numberOfSentences, and sum totalSentimentScore,
                        // to calculate the review's average score.
                        numberOfSentences++;
                        totalSentimentScore += sentenceSentimentScore;

                        // Add the review sentence and assigned score to the current review array.
                        review.add(sentence.toString());
                        review.add(String.valueOf(sentenceSentimentScore));

                        if (devMode) {
                            System.out.println("\nSentence " + counter + ": " + sentence);
                            System.out.println("Sentiment score: " + sentenceSentimentScore);
                            System.out.println("Total sentiment score: " + totalSentimentScore);
                        }
                    }
                    arrayToReturn[counter] = review;
                }
            }
        }

        // Calculate each complete review's average sentiment score.
        averageSentimentScore = totalSentimentScore / numberOfSentences;

        if (devMode) {
            System.out.println("\nFinal sentiment score: " + averageSentimentScore + " / 3");
        }

        // The results to return must be printed to the console so they can be caught with a Node child process.
        System.out.println(Arrays.toString(arrayToReturn));
        return arrayToReturn;
    }

    public static void main(String[] args) {

        String[] allReviews = new String[]{"1", "Review 1", "2", "Review 2", "3", "Review 3"};

        findSentiment(allReviews);

        // set up pipeline properties
//        Properties props = new Properties();

        // set the list of annotators to run
//        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote, sentiment");

        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
//        props.setProperty("coref.algorithm", "neural");

        // build pipeline
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create a document object
//        CoreDocument document = new CoreDocument(reviewText);

        // annnotate the document
//        pipeline.annotate(document);


        // examples

//        // 10th token of the document
//        CoreLabel token = document.tokens().get(10);
//        System.out.println("Example: token");
//        System.out.println(token);
//        System.out.println();
//
//        // text of the first sentence
//        String sentenceText = document.sentences().get(0).reviewText();
//        System.out.println("Example: sentence");
//        System.out.println(sentenceText);
//        System.out.println();
//
//        // second sentence
//        CoreSentence sentence = document.sentences().get(1);
//
//        // list of the part-of-speech tags for the second sentence
//        List<String> posTags = sentence.posTags();
//        System.out.println("Example: pos tags");
//        System.out.println(posTags);
//        System.out.println();
//
//        // list of the ner tags for the second sentence
//        List<String> nerTags = sentence.nerTags();
//        System.out.println("Example: ner tags");
//        System.out.println(nerTags);
//        System.out.println();
//
//        // constituency parse for the second sentence
//        Tree constituencyParse = sentence.constituencyParse();
//        System.out.println("Example: constituency parse");
//        System.out.println(constituencyParse);
//        System.out.println();
//
//        // dependency parse for the second sentence
//        SemanticGraph dependencyParse = sentence.dependencyParse();
//        System.out.println("Example: dependency parse");
//        System.out.println(dependencyParse);
//        System.out.println();
//
//        // kbp relations found in fifth sentence
//        List<RelationTriple> relations =
//                document.sentences().get(4).relations();
//        System.out.println("Example: relation");
//        System.out.println(relations.get(0));
//        System.out.println();
//
//        // entity mentions in the second sentence
//        List<CoreEntityMention> entityMentions = sentence.entityMentions();
//        System.out.println("Example: entity mentions");
//        System.out.println(entityMentions);
//        System.out.println();
//
//        // coreference between entity mentions
//        CoreEntityMention originalEntityMention = document.sentences().get(3).entityMentions().get(1);
//        System.out.println("Example: original entity mention");
//        System.out.println(originalEntityMention);
//        System.out.println("Example: canonical entity mention");
//        System.out.println(originalEntityMention.canonicalEntityMention().get());
//        System.out.println();
//
//        // get document wide coref info
//        Map<Integer, CorefChain> corefChains = document.corefChains();
//        System.out.println("Example: coref chains for document");
//        System.out.println(corefChains);
//        System.out.println();
//
//        // get quotes in document
//        List<CoreQuote> quotes = document.quotes();
//        CoreQuote quote = quotes.get(0);
//        System.out.println("Example: quote");
//        System.out.println(quote);
//        System.out.println();
//
//        // original speaker of quote
//        // note that quote.speaker() returns an Optional
//        System.out.println("Example: original speaker of quote");
//        System.out.println(quote.speaker().get());
//        System.out.println();
//
//        // canonical speaker of quote
//        System.out.println("Example: canonical speaker of quote");
//        System.out.println(quote.canonicalSpeaker().get());
//        System.out.println();

    }
}