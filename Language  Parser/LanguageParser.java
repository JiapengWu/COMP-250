package a2posted;

/**
 * This static class provides the isStatement() method to parse a sequence 
 * of tokens and to decide if it forms a valid statement
 * You are provided with the helper methods isBoolean() and isAssignment().
 * 
 * - You may add other methods as you deem necessary.
 * - You may NOT add any class fields.
 */
public class LanguageParser {
	
	/**
	 * Returns true if the given token is a boolean value, i.e.
	 * if the token is "true" or "false".
	 * 
	 * DO NOT MODIFY THIS METHOD.
	 */
	
	private static boolean isBoolean (String token) {
		
		return (token.equals("true") || token.equals("false"));
		
	}
	
	/**
	 * Returns true if the given token is an assignment statement of the
	 * type "variable=value", where the value is a non-negative integer.
	 * 
	 * DO NOT MODIFY THIS METHOD.
	 */
	private static boolean isAssignment (String token) {
		
		// The code below uses Java regular expressions. You are NOT required to
		// understand Java regular expressions, but if you are curious, see:
		// <http://java.sun.com/javase/6/docs/api/java/util/regex/Pattern.html>
		//
		//   This method returns true iff 
		//   the token matches the following structure:
		//   one or more letters (a variable), followed by
		//   an equal sign '=', followed by
		//   one or more digits.
		//   However, the variable cannot be a keyword ("if", "then", "else", 
		//   "true", "false")
		
		if (token.matches("if=\\d+") || token.matches("then=\\d+") ||
				token.matches("else=\\d+") || token.matches("end=\\d+") ||
				token.matches("true=\\d+") || token.matches("false=\\d+"))
			return false;
		else
			return token.matches("[a-zA-Z]+=\\d+");
		
	}

	/**
	 * Given a sequence of tokens through a StringSplitter object,
	 * this method returns true if the tokens can be parsed according
	 * to the rules of the language as specified in the assignment.
     */
				
	private static boolean isAssignment (String token) {
		
		// The code below uses Java regular expressions. You are NOT required to
		// understand Java regular expressions, but if you are curious, see:
		// <http://java.sun.com/javase/6/docs/api/java/util/regex/Pattern.html>
		//
		//   This method returns true iff 
		//   the token matches the following structure:
		//   one or more letters (a variable), followed by
		//   an equal sign '=', followed by
		//   one or more digits.
		//   However, the variable cannot be a keyword ("if", "then", "else", 
		//   "true", "false")
		
		if (token.matches("if=\\d+") || token.matches("then=\\d+") ||
				token.matches("else=\\d+") || token.matches("end=\\d+") ||
				token.matches("true=\\d+") || token.matches("false=\\d+"))
			return false;
		else
			return token.matches("[a-zA-Z]+=\\d+");
		
	}

	/**
	 * Given a sequence of tokens through a StringSplitter object,
	 * this method returns true if the tokens can be parsed according
	 * to the rules of the language as specified in the assignment.
     */
				
		public static boolean  isStatement(StringSplitter splitter) {

		StringStack stack = new StringStack();
		int count = splitter.countTokens();
		String token;

		if (count == 0)
			return false;

		//   ADD YOUR CODE HERE
		
		int judgeBoolNext=0;
		int judgeEndNext=0;
		int judgeIfNext=0;
		int countIf=0;
		while(splitter.hasMoreTokens()){
			token=splitter.nextToken();
			
			if(count==1){
				if(isAssignment(token)){
					return true;
				}
				else{
					return false;
				}
			}
			
			if(judgeBoolNext==1){
				if(!token.equals("then")){
					return false;
				}
				else{
					judgeBoolNext=0;
				}
			}

			if(judgeIfNext==1){
				if(!isBoolean(token)){
					return false;
				}
				else{
					judgeIfNext=0;
				}
			}
			
			if(token.equals("if")){
				judgeEndNext=0;
				judgeIfNext=1;
				stack.push(token);
				countIf=countIf+1;
			}
			
			else if(token.equals("else")){
				judgeEndNext=0;
				if(stack.isEmpty()){
					return false;
				}
				else{
					String tmp=stack.pop();
					if(!tmp.equals("if")){
					return false;
					}
				}
			}
			//for if and else
			else if(isBoolean(token)){
				judgeBoolNext=1;
				judgeEndNext=0;
				stack.push(token);
			}
			else if(isAssignment(token)){
				if(judgeEndNext==1){
					return false;
				}
				else{
					judgeEndNext=0;
				}
				if(isBoolean(stack.peek())){
					stack.pop();
				}
				else if(stack.isEmpty()){
					stack.push(token);
				}
				else{
					return false;
				}
			}


			//for boolean and assignments
			else if(token.equals("then")){
				judgeEndNext=0;
				if(!isBoolean(stack.peek())){
					return false;
				}
			}
			
			else if(token.equals("end")){
				if(isAssignment(stack.peek())){
					stack.pop();
				}
				judgeEndNext=1;
				countIf=countIf-1;
			}
			else{
				return false;
			}

		}	
		return stack.isEmpty()&&(countIf==0);
	}
}