package com.seedcorn.sentimentAnalysis;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.Properties;

/**
 * This class takes a parameter (review) through the main args and runs it through the sentiment analysis.
 * Where necessary, the analysis breaks the review into shorter sentences and scores each one.
 * If devMode is set to TRUE, the detailed output is displayed in the console.
 * The average of the review sentiments are then returned as a type double.
 */
public class SeedcornSentimentAnalysis {

    private static double findSentiment(String review) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        double totalSentimentScore = 0;
        double numberOfSentences = 0;
        double averageSentimentScore;
        int counter = 1;

        // Set to TRUE to display the breakdown of the output to the console
        boolean devMode = true;

        if (review != null && review.length() > 0) {
            Annotation annotation = pipeline.process(review);

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

            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);

                int sentimentSectionScore = RNNCoreAnnotations.getPredictedClass(tree);

                numberOfSentences++;
                totalSentimentScore += sentimentSectionScore;

                if (devMode) {
                    System.out.println("\nSentence " + counter++ + ": " + sentence);
                    System.out.println("Sentiment score: " + sentimentSectionScore);
                    System.out.println("Total sentiment score: " + totalSentimentScore);
                }
            }
        }
        averageSentimentScore = totalSentimentScore / numberOfSentences;

        if (devMode) {
            System.out.println("\nFinal sentiment score: " + averageSentimentScore + " / 3");
        }
        System.out.println(averageSentimentScore);

        return averageSentimentScore;
    }

    public static void main(String[] args) {

        System.out.println("args: " + args.length);

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