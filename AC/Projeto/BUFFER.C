#include <stdio.h>
#include <stdlib.h>
#include <dos.h>
#include <string.h>
#include "buffer.h"
#include "win.h"

/* Tamanho do buffer */
#define BUF_SIZE 50



/* Definicao do tipo "buffer_t"
 ATENCAO: 'buffer_t' e um apontador para uma estrutura do tipo 'buffer_s' */

/* typedef struct buffer_s *buffer_t;*/

/* So precisa definir a estrutura "buffer_s" no ficheiro "BUFFER.C" */
struct buffer_s {
    unsigned char bytes[BUF_SIZE + 3];
    int put;
};


/* Retorna um buffer devidamente inicializado */
buffer_t initBuffer(void) {
    /* Reserva espaco para o buffer, termina o programa se nao conseguir */
    buffer_t buffer = malloc(sizeof(*buffer));
    if (buffer == NULL) {
        fprintf(stderr,"Error allocating buffer\n");
        exit(1);
    }
    strcpy(buffer->bytes,"");
    disable();
    buffer->put = 0;  /* buffer vazio */
    enable();
    /* Agora 'buffer' e' um apontador valido para um buffer_s */
    return buffer;
}


/* Adiciona o caracter 'c' ao buffer */
int appendBuffer(buffer_t buffer, char c) {

    if(buffer->put >= BUF_SIZE){
        return 1;
    }

    if(c == '\n'){
       buffer->bytes[buffer->put++] = c;

       buffer->bytes[buffer->put++] = '\r';

       buffer->bytes[buffer->put] = '\0';

       win_puts(buffer->bytes);

       disable();
       buffer->put = 0;
       enable();

       strcpy(buffer->bytes,"");

    }else{
       buffer->bytes[buffer->put++] = c;
    }

    if(buffer->put < BUF_SIZE){
        return 0;
    }

    return 1;
}


/* Esvazia o buffer
 Deve simplesmente escrever no terminal os caracteres contidos no buffer */

void flushBuffer(buffer_t buffer) {

   buffer->bytes[buffer->put++] ='\n';

   buffer->bytes[buffer->put++] = '\r';

   buffer->bytes[buffer->put] = '\0';

   disable();
   buffer->put = 0;
   enable();

   win_puts(buffer->bytes);
   strcpy(buffer->bytes,"");
}


