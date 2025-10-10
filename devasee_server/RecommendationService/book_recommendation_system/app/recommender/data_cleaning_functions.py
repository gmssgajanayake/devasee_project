import ast # to convert string into list
import pandas as pd


# convert string list into list for fetchign specific df, always pass a string
def genres_string_to_list_convertor(text, no_of_items=None):

    if isinstance(text, list) :
        return text
        
    # text is null return []
    if isinstance(text, float) and pd.isna(text):
        return []
        
    word_list = []
    count=1
    
    for i in ast.literal_eval(text):
        word_list.append(i['name'])
        if no_of_items is not None:
            if count >= no_of_items:
                break
            count += 1
            
    return word_list

# convert string list into list for fetchign specific df, always pass a string
def keywords_string_to_list_convertor(text, no_of_items=None):

    if isinstance(text, list) :
        return text
        
    # text is null return []
    if isinstance(text, float) and pd.isna(text):
        return []
        
    word_list = []
    count=1
    
    for i in ast.literal_eval(text):
        word_list.append(i)
        if no_of_items is not None:
            if count >= no_of_items:
                break
            count += 1
            
    return word_list

# Reomove spaces betweeen names, always pass a list of str
def remove_spaces(text):
    words = []
    
    for i in text:
        words.append(i.replace(" ", ""))
    return words

# convert words in text list into lower, pass a list of str
def lowercase_words(text):
    if isinstance(text, list):
        return text
    return [i.lower() for i in text]

# wraps it into a single-element list, pass str
def to_list(text):
    if isinstance(text, list):
        return text

    if isinstance(text, str):
        return [text]
    else:
        return []
        