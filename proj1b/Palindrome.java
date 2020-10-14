public class Palindrome {
    public Deque<Character> wordToDeque(String word){
        Deque<Character> dq = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            dq.addLast(word.charAt(i));
        }
        return dq;
    }
    public boolean isPalindrome(String word){
        if (word.length() == 0 || word.length() == 1) {
            return true;
        }
        Palindrome p = new Palindrome();
        Deque<Character> d = p.wordToDeque(word);
        if (d.removeFirst() == d.removeLast()){
            return isPalindrome(dequeToWord(d));
        } else {
            return false;
        }
    }
    public boolean isPalindrome(String word, CharacterComparator cc){
        if (word.length() == 0 || word.length() == 1) {
            return true;
        }
        Palindrome p = new Palindrome();
        Deque<Character> d = p.wordToDeque(word);
        if (cc.equalChars(d.removeFirst(),d.removeLast())){
            return isPalindrome(dequeToWord(d),cc);
        } else {
            return false;
        }
    }

    private String dequeToWord(Deque<Character> d){
        String s = "";
        while (!d.isEmpty()) {
            s += d.removeFirst();
        }
        return s;
    }
}

