#include <stdio.h>
#include <dos.h>
#include <string.h>
#include "BUFFER.H"
#include "win.h"

#define THR 0x3F8
#define LSR 0x3FD
#define RBR 0x3F8
#define MCR 0x3FC
#define IER 0x3F9
#define IMR 0x21
#define ISR 0x20

#define SEND_MASK 0x20
#define EOI 0x20
#define RECIEVE_MASK 0x1

#define MCR_MASK_ON 0x8
#define IER_MASK_ON 0x1
#define IMR_MASK_ON 0x10

#define MCR_MASK_OFF 0xF7
#define IER_MASK_OFF 0xFE
#define IMR_MASK_OFF 0xEF

#define EXIT_CMD "q\n"

void interrupt (*oldisr)();

unsigned char recieveSerial();

buffer_t buffer;

void interrupt isr(){
  unsigned char c;
  int buffFull = 0;

  enable();

  if((inportb(LSR) & RECIEVE_MASK) == RECIEVE_MASK){
      c = inportb(RBR);
      buffFull = appendBuffer(buffer,c);

      if(buffFull == 1){
	 flushBuffer(buffer);
      }

  }

  outport(ISR, EOI);
}

void interruptOn(){
  unsigned char status;

  oldisr = getvect(12);
  setvect(12,isr);

  status = inportb(MCR);
  status = MCR_MASK_ON | status;
  outportb(MCR, status);

  status = inportb(IER);
  status = IER_MASK_ON | status;
  outportb(IER,status);

  disable();

  status = inportb(IMR);
  status = IMR_MASK_OFF & status;
  outportb(IMR,status);

  enable();

}

void interruptOff(){
  unsigned char status;

  status = inportb(MCR);
  status = MCR_MASK_OFF & status;
  outportb(MCR,status);

  status = inportb(IER);
  status = IER_MASK_OFF & status;
  outportb(IER,status);

  disable();

  status = inportb(IMR);
  status = IMR_MASK_ON | status;
  outportb(IMR,status);

  setvect(12,oldisr);

  enable();

}


void sendSerial( unsigned char byte ) {
   unsigned char statusReg = inportb(LSR);

   while((statusReg & SEND_MASK) != SEND_MASK){
      statusReg = inportb(LSR);
   }
   outportb(THR, byte);

}


int main( int argc, char *argv[] ) {
  char msg[50];
  int i = 0;
  buffer = initBuffer();
  interruptOn();
  win_init();

  while( fgets(msg,50,stdin) != NULL){
    if(strcmp(msg,EXIT_CMD) != 0){
	while (msg[i] != '\0'){
	   sendSerial(msg[i++]);
	}
	win_puts(msg);
	win_delbox();
	i = 0;
     }else{
	break;
     }
   }

   interruptOff();
   return 0;
}
