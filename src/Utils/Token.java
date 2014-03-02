//------------------------------------------------------------------//
//                        COPYRIGHT NOTICE                          //
//------------------------------------------------------------------//
// Copyright (c) 2008, Francisco Jos� Moreno Velo                   //
// All rights reserved.                                             //
//                                                                  //
// Redistribution and use in source and binary forms, with or       //
// without modification, are permitted provided that the following  //
// conditions are met:                                              //
//                                                                  //
// * Redistributions of source code must retain the above copyright //
//   notice, this list of conditions and the following disclaimer.  // 
//                                                                  //
// * Redistributions in binary form must reproduce the above        // 
//   copyright notice, this list of conditions and the following    // 
//   disclaimer in the documentation and/or other materials         // 
//   provided with the distribution.                                //
//                                                                  //
// * Neither the name of the University of Huelva nor the names of  //
//   its contributors may be used to endorse or promote products    //
//   derived from this software without specific prior written      // 
//   permission.                                                    //
//                                                                  //
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND           // 
// CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,      // 
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF         // 
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE         // 
// DISCLAIMED. IN NO EVENT SHALL THE COPRIGHT OWNER OR CONTRIBUTORS //
// BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,         // 
// EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED  //
// TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,    //
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND   // 
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT          //
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING   //
// IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF   //
// THE POSSIBILITY OF SUCH DAMAGE.                                  //
//------------------------------------------------------------------//

//------------------------------------------------------------------//
//                      Universidad de Huelva                       //
//           Departamento de Tecnolog�as de la Informaci�n          //
//   �rea de Ciencias de la Computaci�n e Inteligencia Artificial   //
//------------------------------------------------------------------//
//                     PROCESADORES DE LENGUAJE                     //
//------------------------------------------------------------------//
//                                                                  //
//          Compilador del lenguaje Tinto [Versi�n 0.0]             //
//                                                                  //
//------------------------------------------------------------------//

package Utils;

/**
 * Clase que describe un componente l�xico
 *  
 *
 */
public class Token  {
	
	/**
	 * Constante que identifica la categor�a l�xica de final de entrada
	 */
	public static final int EOF = 0;
	
	/**
	 * Tipo de componente l�xico.
	 * Identificador de la categor�a l�xica del componente.
	 */
	private int _kind;
	
	/**
	 * Lexema que da origen al componente
	 */
	private String _lexeme;
	
	/**
	 * N�mero de fila en la que se encuentra el inicio del componente
	 */
	private int _row;
	
	/**
	 * N�mero de columna en la que se encuentra el inicio del componente
	 */
	private int _column;
	

	/**
	 * Constructor
	 * @param kind Identificador de la categor�a l�xica
	 * @param lexeme Lexema que origina el componente
	 * @param row Fila en la que comienza el componente
	 * @param column Columna en la que comienza el componente
	 */
	public Token(int kind, String lexeme, int row) {
		_kind = kind;
		_lexeme = lexeme;
		_row = row;
	}
	
	public Token(){ 
		_kind = 0;
		_lexeme = new String();
		_row = -1;
	}
	
	public void setKind(int kind){
		_kind = kind;
	}
	
	public void setLexeme(String lex){
		_lexeme = lex;
	}
	
	public void setRow(int row){
		_row = row;
	}
	

	
	
	/**
	 * Obtiene el identificador de categoria
	 * @return Tipo de componente
	 */
	public int getKind() {
		return this._kind;
	}
	
	/**
	 * Obtiene el lexema del componente
	 * @return Lexema del componente
	 */
	public String getLexeme() {
		return _lexeme;
	}
	
	/**
	 * Obtiene la fila de inicio del componente
	 * @return Fila de inicio del componente
	 */
	public int getRow() {
		return _row;
	}
	
	/**
	 * Obtiene la columna de inicio del componente
	 * @return Columna de inicio del componente
	 */
	public int getColumn() {
		return _column;
	}
	
	public void addCharToLexeme(char c){
		_lexeme += c;
	}
	
	/**
	 * Obtiene una descripci�n del Token
	 */
	@Override
	public String toString() {
		return "[Row: "+_row+"][Kind: "+_kind+"] "+_lexeme;
	}
	
	@Override
	public Token clone(){
		Token t = new Token(_kind, _lexeme, _row);
		return t;
	}
}
