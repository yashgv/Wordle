import static com.raylib.Jaylib.*;
import static com.raylib.Raylib.*;
import java.util.*;



public class Wordle implements global{
    public static void main(String args[]) {
        Game game=new Game();
        Letters arr[][]=new Letters[6][5];
        int tempCol=0;
        int row=0;
        int col=4;
        boolean win=false,ans=false;

        for(int i=0;i<6;i++){
            for(int j=0;j<5;j++){
                arr[i][j] =new Letters();
            }
        }

        
        InitWindow(1280,720,"WORDLE");
        while (!WindowShouldClose()) {
            BeginDrawing();
            ClearBackground(DARKGRAY);
            DrawRectangle( globalX+10, globalY+10, 385, 60, BLACK);
            DrawRectangleLines(globalX+20, globalY+15, 365, 50,LIGHTGRAY);
            DrawText("Guess the word?", globalX+80, globalY+25, 30, WHITE);

            if(ans && win==false){   //Revealing the ans and the lose
                DrawRectangle( globalX+10,globalY+ 570, 385, 60, BLACK);
                DrawRectangleLines(globalX+20,globalY+ 575, 365, 50,LIGHTGRAY);
                DrawText(game.randomWord, globalX+140,globalY+ 585, 30, WHITE);

                DrawRectangle( globalX+10,globalY+ 635, 385, 60, BLACK);
                DrawRectangleLines(globalX+20,globalY+ 640, 365, 50,LIGHTGRAY);
                DrawText("REPLAY",globalX+ 140,globalY+ 650, 30, WHITE);

                if(IsKeyPressed(KEY_ESCAPE)){
                    CloseWindow();
                }
                if(game.DetectClick()){
                    game=new Game();
                    arr=new Letters[6][5];
                    tempCol=0;
                    row=0;
                    col=4;
                    win=false;
                    ans=false;

                    for(int i=0;i<6;i++){
                        for(int j=0;j<5;j++){
                            arr[i][j] =new Letters();
                        }
                    }
                }

            }
            //Squares in which letters are stored
            DrawRectangle(globalX+0,globalY+80,405,485,BLACK);
            for(int i=0;i<6;i++){
                for(int j=0;j<5;j++){
                    DrawRectangle(globalX+5+(80*j),globalY+85+(80*i),75,75, ((arr[i][j].color[0])?LIGHTGRAY:(arr[i][j].color[1])?YELLOW:(arr[i][j].color[2])?GREEN:GRAY));
                    DrawRectangleLines(globalX+5+(80*j),globalY+85+(80*i),75,75, ((arr[i][j].color[0])?WHITE:(arr[i][j].color[1])?YELLOW:(arr[i][j].color[2])?GREEN:GRAY));
                }
            }
            
            //Typer
            int ch= GetCharPressed();
            if(ch!=0 && tempCol<5 && row<6){
                arr[row][tempCol].l=(""+(char)ch).toUpperCase().charAt(0);
                tempCol++;
            }

            //Submission
            if(IsKeyPressed(KEY_ENTER) && tempCol==5){
                game.check(arr,row);
                if(game.win(arr,row)){
                    win=true;
                }
                tempCol=0;
                if(row+1>5) {  //after all attempts reveal answer
                    ans=true;
                    continue;
                }
                row++;
            }
            if(IsKeyPressed(KEY_BACKSPACE)){
                if(tempCol>0)tempCol--;
                arr[row][tempCol].l='_';
            }
            // for(int j=0;j<5;j++) System.out.println(arr[i][j]);
            for(int i=0;i<=row;i++){
                for(int j=0;j<=col;j++){
                    DrawText(""+arr[i][j].l,globalX+ 35+(80*j), 105+(80*i),30, BLACK);
                }
            }
            if(win){
                DrawRectangle(globalX+ 10,globalY+ 570, 385, 60, BLACK);
                DrawRectangleLines(globalX+20, globalY+575, 365, 50,LIGHTGRAY);
                DrawText("WON Correct Guess!",globalX+ 50, globalY+585, 30, WHITE);


                DrawRectangle(globalX+ 10,globalY+ 635, 385, 60, BLACK);
                DrawRectangleLines(globalX+20, globalY+640, 365, 50,LIGHTGRAY);
                DrawText("REPLAY", globalX+140, globalY+650, 30, WHITE);
                if(game.DetectClick()){
                    game=new Game();
                    arr=new Letters[6][5];
                    tempCol=0;
                    row=0;
                    col=4;
                    win=false;
                    ans=false;

                    for(int i=0;i<6;i++){
                        for(int j=0;j<5;j++){
                            arr[i][j] =new Letters();
                        }
                    }
                }
                
            }
            if(IsKeyPressed(KEY_ESCAPE)){
                CloseWindow();
            }
            EndDrawing();
        }
        
    }
}

interface global{
    int globalX=400;
    int globalY=10;
}

class Letters{  //Object to store array and color
    char l;
    boolean color[]={true,false,false,false};
}

class Game implements global{
    Scanner scanner = new Scanner(System.in);
    Random random = new Random();
    String[] words = {"prize","frost","jelly","baked","swirl","mirth","giddy","funky","crisp","plumb","quilt","storm","chalk","joust","prism","shred","crave","haste","fable","quirk","lodge","flask","plaid","tense","swift","blaze","gloom","rival","crest","drown","slide","frown","slope","jokes","zebra","gears","bloom","ghost","spire","snail","toast","latch","skirt","brave","quiet","yield","tread","solar","pluto","evoke","snack","wrist","shave","thump","fiery","chess","coral","pulse","diver","yacht","spine","stove","swine","chirp","spade","exile","crown","vowel","haste","prism"};


    // Choose a random word from the list
    String word=words[random.nextInt(words.length)];
    String randomWord = word.toUpperCase();
    // String randomWord = "HELLO";

    
    //Function for button
    boolean CanClick(){
        int X=GetMouseX();
        int Y= GetMouseY();       
        if (X>= globalX+10 && X <= (globalX+10 + 385))
        {
            if (Y >= globalY+635 && Y <= globalY+635 + 60)
            {
                return true;
            }
        }
        return false;
    }
    boolean DetectClick(){
        int currentGest= GetGestureDetected();   
        if(IsMouseButtonPressed(1) || currentGest==GESTURE_DOUBLETAP || currentGest==GESTURE_TAP)
        {
            if(CanClick())
            {
                return true;

            }
        }
        return false;
    }

    


    boolean win(Letters arr[][],int row){  //To check whether the word is fully correct or not
        for(int i=0;i<5;i++){
            if(!arr[row][i].color[2]) return false;
        }
        return true;
    }

    void check(Letters arr[][],int row){  //Check the Submission

        //TO make the yellow and Green
        for(int i=0;i<5;i++){
            // System.out.println(arr[row][i].l);
            if(arr[row][i].l==randomWord.charAt(i) && arr[row][i].color[2]==false){
                arr[row][i].color[2]=true;
                arr[row][i].color[0]=false;
                arr[row][i].color[1]=false;
            }
            else{
                for(int j=0;j<5;j++){
                    if(arr[row][i].l==randomWord.charAt(j) && arr[row][i].color[2]==false){
                        arr[row][i].color[1]=true;
                        arr[row][i].color[0]=false;
                    }

                }
            }
        }
        //To check the multiple occurrences in the program  CRUCIAL BUT IMPORTANT !!!!!!!
        for(int i=0;i<5;i++){
            int count=1;
            int occ=0;
            for(int y=0;y<5;y++){
                if(y!=i && randomWord.charAt(i)==randomWord.charAt(y)){
                    count++;
                }
            }
            for(int j=0;j<5;j++){
                if(randomWord.charAt(i)==arr[row][j].l && arr[row][j].color[2]==true){
                    occ++;
                }
            }
            for(int j=0;j<5;j++){
                if(randomWord.charAt(i)==arr[row][j].l && arr[row][j].color[1]==true && occ<count){
                    occ++;
                }
                else if(randomWord.charAt(i)==arr[row][j].l && occ<=5){
                    arr[row][j].color[1]=false;
                    arr[row][j].color[3]=true;
                    occ++;
                }
            }
            
            if(occ>count){
                occ=0;
                for(int x=0;x<5;x++){
                    if(arr[row][x].l==randomWord.charAt(i) && arr[row][x].color[2]==true){
                        occ++;
                    }
                }
            }
            for(int x=0;x<5;x++){
                if(occ!=count && arr[row][x].l==randomWord.charAt(i)){
                    occ--;
                }
                else if(occ<count && arr[row][x].l==randomWord.charAt(i) && x!=i){
                    arr[row][x].color[1]=false;
                    arr[row][x].color[3]=true;
                }

            }
        }

        //to GRAY out the words not in the randomword
        for(int i=0;i<5;i++){
            if(arr[row][i].color[0]==true){
                arr[row][i].color[0]=false;
                arr[row][i].color[3]=true;
            }
        }
    }
}

