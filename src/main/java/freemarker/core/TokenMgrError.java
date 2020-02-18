/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package freemarker.core;

import freemarker.template.Template;

/**
 * Exception thrown on lower (lexical) level parsing errors. Shouldn't reach normal FreeMarker users, as FreeMarker
 * usually catches this and wraps it into a {@link ParseException}.
 * 
 * This is a modified version of file generated by JavaCC from FTL.jj.
 * You can modify this class to customize the error reporting mechanisms so long as the public interface
 * remains compatible with the original.
 * 
 * @see ParseException
 */
public class TokenMgrError extends Error {
   /*
    * Ordinals for various reasons why an Error of this type can be thrown.
    */

   /**
    * Lexical error occurred.
    */
   static final int LEXICAL_ERROR = 0;

   /**
    * An attempt was made to create a second instance of a static token manager.
    */
   static final int STATIC_LEXER_ERROR = 1;

   /**
    * Tried to change to an invalid lexical state.
    */
   static final int INVALID_LEXICAL_STATE = 2;

   /**
    * Detected (and bailed out of) an infinite loop in the token manager.
    */
   static final int LOOP_DETECTED = 3;

   /**
    * Indicates the reason why the exception is thrown. It will have
    * one of the above 4 values.
    */
   int errorCode;

   private String detail;
   private Integer lineNumber, columnNumber;
   private Integer endLineNumber, endColumnNumber;

   /**
    * Replaces unprintable characters by their espaced (or unicode escaped)
    * equivalents in the given string
    */
   protected static final String addEscapes(String str) {
      StringBuilder retval = new StringBuilder();
      char ch;
      for (int i = 0; i < str.length(); i++) {
        switch (str.charAt(i))
        {
           case 0 :
              continue;
           case '\b':
              retval.append("\\b");
              continue;
           case '\t':
              retval.append("\\t");
              continue;
           case '\n':
              retval.append("\\n");
              continue;
           case '\f':
              retval.append("\\f");
              continue;
           case '\r':
              retval.append("\\r");
              continue;
           case '\"':
              retval.append("\\\"");
              continue;
           case '\'':
              retval.append("\\\'");
              continue;
           case '\\':
              retval.append("\\\\");
              continue;
           default:
              if ((ch = str.charAt(i)) < 0x20 || ch > 0x7e) {
                 String s = "0000" + Integer.toString(ch, 16);
                 retval.append("\\u" + s.substring(s.length() - 4, s.length()));
              } else {
                 retval.append(ch);
              }
              continue;
        }
      }
      return retval.toString();
   }

   /**
    * Returns a detailed message for the Error when it's thrown by the
    * token manager to indicate a lexical error.
    * Parameters : 
    *    EOFSeen     : indicates if EOF caused the lexicl error
    *    curLexState : lexical state in which this error occurred
    *    errorLine   : line number when the error occurred
    *    errorColumn : column number when the error occurred
    *    errorAfter  : prefix that was seen before this error occurred
    *    curchar     : the offending character
    * Note: You can customize the lexical error message by modifying this method.
    */
   protected static String LexicalError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar) {
      return("Lexical error: encountered " +
           (EOFSeen ? "<EOF> " : ("\"" + addEscapes(String.valueOf(curChar)) + "\"") + " (" + (int) curChar + "), ") +
           "after \"" + addEscapes(errorAfter) + "\".");
   }

   /**
    * You can also modify the body of this method to customize your error messages.
    * For example, cases like LOOP_DETECTED and INVALID_LEXICAL_STATE are not
    * of end-users concern, so you can return something like : 
    *
    *     "Internal Error : Please file a bug report .... "
    *
    * from this method for such cases in the release version of your parser.
    */
   @Override
public String getMessage() {
      return super.getMessage();
   }

   /*
    * Constructors of various flavors follow.
    */

   public TokenMgrError() {
   }

   public TokenMgrError(String detail, int reason) {
      super(detail);  // the "detail" must not contain location information, the "message" might does
      this.detail = detail;
      errorCode = reason;
   }

   /**
    * @since 2.3.20
    * 
    * @deprecated If you know the end position, use {@link #TokenMgrError(String, int, int, int, int, int)} instead.
    */
   @Deprecated
public TokenMgrError(String detail, int reason, int errorLine, int errorColumn) {
       this(detail, reason, errorLine, errorColumn, 0, 0);
       this.endLineNumber = null; 
       this.endColumnNumber = null; 
    }
   
   /**
    * @since 2.3.21
    */
   public TokenMgrError(String detail, int reason,
           int errorLine, int errorColumn,
           int endLineNumber, int endColumnNumber) {
       super(detail);  // the "detail" must not contain location information, the "message" might does
       this.detail = detail;
       errorCode = reason;
       
       this.lineNumber = Integer.valueOf(errorLine);  // In J2SE there was no Integer.valueOf(int)
       this.columnNumber = Integer.valueOf(errorColumn);
       this.endLineNumber = Integer.valueOf(endLineNumber); 
       this.endColumnNumber = Integer.valueOf(endColumnNumber); 
    }

   /**
    * Overload for JavaCC 6 compatibility.
    * 
    * @since 2.3.24
    */
   TokenMgrError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, int curChar, int reason) {
       this(EOFSeen, lexState, errorLine, errorColumn, errorAfter, (char) curChar, reason);
   }
   
   public TokenMgrError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar, int reason) {
      this(LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
      
      this.lineNumber = Integer.valueOf(errorLine);  // In J2SE there was no Integer.valueOf(int)
      this.columnNumber = Integer.valueOf(errorColumn);
      // We blame the single character that can't be the start of a legal token: 
      this.endLineNumber = this.lineNumber; 
      this.endColumnNumber = this.columnNumber; 
   }

   /**
    * 1-based line number of the unexpected character(s).
    * 
    * @since 2.3.20
    */
   public Integer getLineNumber() {
      return lineNumber;
   }
    
   /**
    * 1-based column number of the unexpected character(s).
    * 
    * @since 2.3.20
    */
   public Integer getColumnNumber() {
      return columnNumber;
   }
   
   /**
    * Returns the 1-based line at which the last character of the wrong section is. This will be usually (but not
    * always) the same as {@link #getLineNumber()} because the lexer can only point to the single character that
    * doesn't match any patterns.
    * 
    * @since 2.3.21
    */
   public Integer getEndLineNumber() {
      return endLineNumber;
   }

   /**
    * Returns the 1-based column at which the last character of the wrong section is. This will be usually (but not
    * always) the same as {@link #getColumnNumber()} because the lexer can only point to the single character that
    * doesn't match any patterns.
    * 
    * @since 2.3.21
    */
   public Integer getEndColumnNumber() {
      return endColumnNumber;
   }

   public String getDetail() {
       return detail;
   }

   public ParseException toParseException(Template template) {
       return new ParseException(getDetail(),
               template,
               getLineNumber() != null ? getLineNumber().intValue() : 0,
               getColumnNumber() != null ? getColumnNumber().intValue() : 0,
               getEndLineNumber() != null ? getEndLineNumber().intValue() : 0,
               getEndColumnNumber() != null ? getEndColumnNumber().intValue() : 0);
   }
   
}
