package com.seedcorn.sentimentAnalysis;

/**
 * SeedCorn Android review sentiment analyser, using the Stanford Core NLP library.
 *
 * @author Richard Denton
 *
 * This SeedcornSentimentAnalysis class is designed to be compiled into a JAR file and used with the accompanying
 * ReactJS/Node.js application.
 * It is passed a single string through the main 'String[] args' containing the review Id's, review text, and a custom
 * delimiter to separate each element throughout the string.
 * The string is split and creates a 'String[] splitReviews' for each new review adding the review ID and review text.
 * The splitReview array is iterated through adding the review ID to 'String reviewsToReturn' and passes the reviewText
 * through for analysis.
 * Where necessary, the analyser breaks the reviewText into shorter sentences, assigns a score, and adds both values
 * to the reviewsToReturn string.
 *
 * @Return
 * The reviewsToReturn string containing all the reviews is returned.
 * Each review is made up in the following format:
 * id 'delimiter' sentence 'delimiter' score 'delimiter' 'reviewDelimiter'
 * The sentence and score sections can repeat as many times as is returned from the analysis.
 */

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.Properties;

public class SeedcornSentimentAnalysis {
    private static String findSentiment(String allReviews) {
        // If devMode is set to TRUE, the detailed output of analysis is displayed in the console.
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

        if (devMode) {
            System.out.println("Incoming String of allReviews: " + allReviews);
        }

        double totalSentimentScore = 0;
        double numberOfSentences = 0;
        double averageSentimentScore;
        int counter = 1;

        //Setup Stanford Core NLP library
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Create a reviewsToReturn string to add all of the review ID's, processed reviews, and scores to.
        // Split the allReviews string by the delimiter and store in an array (splitReviews).
        // Iterate through the splitReviews and process by index:
        // - - evens -> add the current splitReview as the review id.
        // - - odds -> pass reviewText for analysing and add to reviewsToReturn as sentences, along with their scores.
        String delimiter = "!#delimiter#!";
        String reviewDelimiter = "!#reviewDelimiter#!";
        String reviewsToReturn = "";
        String[] splitReviews = allReviews.split(delimiter);
        String reviewText;
        for (int i = 0; i < splitReviews.length; i++) {
            if (devMode) {
                System.out.println("\n===== ===== ===== Main loop ===== ===== =====");
                System.out.println("Split string to process: " + splitReviews[i]);
            }

            // If even -> add the current splitReview as the review id.
            // If odd -> pass reviewText for analysing and add to reviewsToReturn as sentences, along with their scores.
            if ((i & 1) == 0) {
                reviewsToReturn += splitReviews[i];
                reviewsToReturn += delimiter;
            } else {

                reviewText = splitReviews[i];

                if (reviewText != null && reviewText.length() > 0) {
                    Annotation annotation = pipeline.process(reviewText);

                    // Analyse the review, and for each sentence found, run the sentiment analysis.
                    // Add the sentence and score to the reviewsToReturn string.
                    for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                        if (devMode) {
                            System.out.println("\n===== Analysis loop =====");
                        }

                        Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                        int sentenceSentimentScore = RNNCoreAnnotations.getPredictedClass(tree);

                        // Increment numberOfSentences, and sum totalSentimentScore,
                        // to calculate review's average score.
                        numberOfSentences++;
                        totalSentimentScore += sentenceSentimentScore;

                        // Add review sentence and score to reviewsToReturn string.
                        reviewsToReturn += sentence.toString();
                        reviewsToReturn += delimiter;
                        reviewsToReturn += String.valueOf(sentenceSentimentScore);
                        reviewsToReturn += delimiter;


                        if (devMode) {
                            System.out.println("\nSentence " + counter++ + ": " + sentence);
                            System.out.println("Sentiment score: " + sentenceSentimentScore);
                            System.out.println("Total sentiment score: " + totalSentimentScore);
                        }
                    }

                    reviewsToReturn += reviewDelimiter;
                }
            }
        }

        // Calculate each complete review's average sentiment score.
        averageSentimentScore = totalSentimentScore / numberOfSentences;

        if (devMode) {
            System.out.println("\nFinal sentiment score: " + averageSentimentScore + " / 3");
        }

        // The results to return must be printed to the console so they can be caught with a Node child process.
        System.out.println(reviewsToReturn);
        return reviewsToReturn;
    }

    public static void main(String[] args) {

        String mockReviews = "" +
                "1!#delimiter#!" +
                "I'm a good test review.!#delimiter#!" +
                "2!#delimiter#!" +
                "I'm a bad test review. Made up of sentences.!#delimiter#!";

        findSentiment(args[0]);

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