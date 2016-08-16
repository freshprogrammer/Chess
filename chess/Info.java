/* 
 * Project   : Board Games
 * Package   : chess
 * File Name : Info.java
 * Created By: Douglas Crafts
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Oct 21, 2005 2:45:53 PM
 * Last updated : 05/08/06
 */
package chess;

import java.awt.*;

public final class Info
{	
	//----//Chess Game
	//--Sizing Variables
	public final static int chessFrameSize = 500;
	//original size Chess Frame
	public final static int chessMaxSize = 1500;
	//Maximum size Possible
	
	//--Colors
	public final static Color chessTeam1Color = new Color(255,255,255);
	//color of team 1's pieces
	public final static Color chessTeam2Color = new Color(0,0,0);
	//color of team 2's pieces
	
	//----//Board
	//--Colors
	public final static Color boardBackgroundColor = new Color(0,120,0);
	public final static Color boardProtectColor = new Color(0,0,255);
	public final static Color boardHighlightColor = new Color(0,255,0);
	public final static Color boardKillColor = new Color(255,0,0);
	public final static Color boardSquareColor1 = new Color(226,191,99);
	public final static Color boardSquareColor2 = new Color(163,105,21);
	public final static Color[] boardSquareColor = {boardSquareColor1,boardSquareColor2};
	//color of each set of squares
	public final static Color boardLineColor = Color.black;
	//color of lines around the squares
	public final static Color boardBorderColor = new Color(64,33,2);
	//color of the surounding border
	public final static Color boardLetterColor = Color.LIGHT_GRAY;
	//color of the letters
	
	//--Fonts
	public final static Font boardLetterFont  = new Font("serif",Font.BOLD,50);
	//Font of numbers & letters around the board//this size is overwritted
	
	//--Sizing Variables
	public final static byte boardRanks = 8;
	//L<->R rows   :numbered 1-8, White to Black
	public final static byte boardFiles = 8;
	//U<->D colombs:lettered a-h, Left to Right
	public final static double boardBorderFrac = .6;
	//frac of square size for border -- only works as a decimal
	public final static double boardLineFrac = .035;
	//frac of square size for lines -- only works as a decimal
	public final static double boardLetterFrac = .4;
	//frac of square size squareHighlights -- only works as a decimal
	public final static double boardHighlightFrac = .1;
	//frac of square size for letters -- only works as a decimal
	public final static short boardMinSize = 400;
	//min size of board when resizing
	
	//--//Piece
	//--Specific Piece MoveCodes
	public final static String pieceCastleLeftCode = "CastlingLeft";
	//code for Castling on the king's side
	public final static String pieceCastleRightCode = "CastlingRight";
	//code for Castling on the Queen's side
	public final static String piecePromotionCode = "Promotion";
	//code for Promoting Pawns
	public final static String pieceEmPassantCode = "EnPassant";
	//code for Empasant Move
	public final static String piecePawnDoubleMoveCode = "PawnDoubleMove";
	//code for Pawn Double Move
	
	//--Colors
	public final static Color pieceLineColor = Color.black;
	//color of lines around the squares
	
	//--//Console
	//--Colors
	public final static Color consoleTextColor = Color.black;
	//color of text
	public final static Color consoleBackgroundColor = new Color(225,225,225);
	//color of console Background
}
