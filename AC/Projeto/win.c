/****
 *  WIN.C
 *  vad - Maio 2016 - AC/MIEI - FCT/UNL
 *  usa conio para manipular o ecra diretamente
 *  e simular duas janelas como num chat
 ****/

#include <conio.h>

#include "win.h"

int l;  /* linha corrente na janela superior */


void win_init() {
  int i;
  l=1;
  clrscr();
  gotoxy(1,23);
  cputs("--------------------------------------------------------------\r\n");
}

void win_delbox() {
  gotoxy(1,24);
  delline();
  delline();
}


void win_puts(char*s) {
  int x=wherex();
  int y=wherey();

  if ( l>22 ) {
    movetext(1,2,80,22,1,1);
    l=22;
  }
  gotoxy(1,l);
  clreol();

  cputs(s);

  l=wherey();
  gotoxy(x,y);
}
