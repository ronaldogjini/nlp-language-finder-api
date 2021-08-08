# NLP Language Finder API

This is the backend of the Language Finder program. Built on Java, it makes use of the **n-gram model**  to construct models for each language. For each language, the program reads to files for that particular language, tokenizes them into individual words and creates the histogram/frequency distribution.

Once the frequency distribution is created for each language, together with the text entered by the user, the program calculates the document distance (cosine similarity) to find out which language most closely matches the submitted text.

## Multi-threading
The program makes use of multiple threads during the scanning process of language files. This makes the initial process much faster, and allows for a faster building time of the frequency distribution

## Accuracy
The more language files are used to create the language frequency distribution, the better the result will be. This example comes with 10 languages, and each language folder has 10 text files with sample text of that language. A planned release in the future will store the histograms for future use, while feeding them with new text to improve them even further.