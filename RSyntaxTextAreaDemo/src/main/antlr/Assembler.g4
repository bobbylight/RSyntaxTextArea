/*

 Source: https://github.com/markbush/6502-assembler/blob/master/src/main/antlr4/org/bushnet/Assembler.g4

 Written by Mark Bush <mark@bushnet.org>

 [The "Apache Licence 2.0"]
                                 Apache License
                           Version 2.0, January 2004
                        http://www.apache.org/licenses/

   TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION

   1. Definitions.

      "License" shall mean the terms and conditions for use, reproduction,
      and distribution as defined by Sections 1 through 9 of this document.

      "Licensor" shall mean the copyright owner or entity authorized by
      the copyright owner that is granting the License.

      "Legal Entity" shall mean the union of the acting entity and all
      other entities that control, are controlled by, or are under common
      control with that entity. For the purposes of this definition,
      "control" means (i) the power, direct or indirect, to cause the
      direction or management of such entity, whether by contract or
      otherwise, or (ii) ownership of fifty percent (50%) or more of the
      outstanding shares, or (iii) beneficial ownership of such entity.

      "You" (or "Your") shall mean an individual or Legal Entity
      exercising permissions granted by this License.

      "Source" form shall mean the preferred form for making modifications,
      including but not limited to software source code, documentation
      source, and configuration files.

      "Object" form shall mean any form resulting from mechanical
      transformation or translation of a Source form, including but
      not limited to compiled object code, generated documentation,
      and conversions to other media types.

      "Work" shall mean the work of authorship, whether in Source or
      Object form, made available under the License, as indicated by a
      copyright notice that is included in or attached to the work
      (an example is provided in the Appendix below).

      "Derivative Works" shall mean any work, whether in Source or Object
      form, that is based on (or derived from) the Work and for which the
      editorial revisions, annotations, elaborations, or other modifications
      represent, as a whole, an original work of authorship. For the purposes
      of this License, Derivative Works shall not include works that remain
      separable from, or merely link (or bind by name) to the interfaces of,
      the Work and Derivative Works thereof.

      "Contribution" shall mean any work of authorship, including
      the original version of the Work and any modifications or additions
      to that Work or Derivative Works thereof, that is intentionally
      submitted to Licensor for inclusion in the Work by the copyright owner
      or by an individual or Legal Entity authorized to submit on behalf of
      the copyright owner. For the purposes of this definition, "submitted"
      means any form of electronic, verbal, or written communication sent
      to the Licensor or its representatives, including but not limited to
      communication on electronic mailing lists, source code control systems,
      and issue tracking systems that are managed by, or on behalf of, the
      Licensor for the purpose of discussing and improving the Work, but
      excluding communication that is conspicuously marked or otherwise
      designated in writing by the copyright owner as "Not a Contribution."

      "Contributor" shall mean Licensor and any individual or Legal Entity
      on behalf of whom a Contribution has been received by Licensor and
      subsequently incorporated within the Work.

   2. Grant of Copyright License. Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      copyright license to reproduce, prepare Derivative Works of,
      publicly display, publicly perform, sublicense, and distribute the
      Work and such Derivative Works in Source or Object form.

   3. Grant of Patent License. Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      (except as stated in this section) patent license to make, have made,
      use, offer to sell, sell, import, and otherwise transfer the Work,
      where such license applies only to those patent claims licensable
      by such Contributor that are necessarily infringed by their
      Contribution(s) alone or by combination of their Contribution(s)
      with the Work to which such Contribution(s) was submitted. If You
      institute patent litigation against any entity (including a
      cross-claim or counterclaim in a lawsuit) alleging that the Work
      or a Contribution incorporated within the Work constitutes direct
      or contributory patent infringement, then any patent licenses
      granted to You under this License for that Work shall terminate
      as of the date such litigation is filed.

   4. Redistribution. You may reproduce and distribute copies of the
      Work or Derivative Works thereof in any medium, with or without
      modifications, and in Source or Object form, provided that You
      meet the following conditions:

      (a) You must give any other recipients of the Work or
          Derivative Works a copy of this License; and

      (b) You must cause any modified files to carry prominent notices
          stating that You changed the files; and

      (c) You must retain, in the Source form of any Derivative Works
          that You distribute, all copyright, patent, trademark, and
          attribution notices from the Source form of the Work,
          excluding those notices that do not pertain to any part of
          the Derivative Works; and

      (d) If the Work includes a "NOTICE" text file as part of its
          distribution, then any Derivative Works that You distribute must
          include a readable copy of the attribution notices contained
          within such NOTICE file, excluding those notices that do not
          pertain to any part of the Derivative Works, in at least one
          of the following places: within a NOTICE text file distributed
          as part of the Derivative Works; within the Source form or
          documentation, if provided along with the Derivative Works; or,
          within a display generated by the Derivative Works, if and
          wherever such third-party notices normally appear. The contents
          of the NOTICE file are for informational purposes only and
          do not modify the License. You may add Your own attribution
          notices within Derivative Works that You distribute, alongside
          or as an addendum to the NOTICE text from the Work, provided
          that such additional attribution notices cannot be construed
          as modifying the License.

      You may add Your own copyright statement to Your modifications and
      may provide additional or different license terms and conditions
      for use, reproduction, or distribution of Your modifications, or
      for any such Derivative Works as a whole, provided Your use,
      reproduction, and distribution of the Work otherwise complies with
      the conditions stated in this License.

   5. Submission of Contributions. Unless You explicitly state otherwise,
      any Contribution intentionally submitted for inclusion in the Work
      by You to the Licensor shall be under the terms and conditions of
      this License, without any additional terms or conditions.
      Notwithstanding the above, nothing herein shall supersede or modify
      the terms of any separate license agreement you may have executed
      with Licensor regarding such Contributions.

   6. Trademarks. This License does not grant permission to use the trade
      names, trademarks, service marks, or product names of the Licensor,
      except as required for reasonable and customary use in describing the
      origin of the Work and reproducing the content of the NOTICE file.

   7. Disclaimer of Warranty. Unless required by applicable law or
      agreed to in writing, Licensor provides the Work (and each
      Contributor provides its Contributions) on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
      implied, including, without limitation, any warranties or conditions
      of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A
      PARTICULAR PURPOSE. You are solely responsible for determining the
      appropriateness of using or redistributing the Work and assume any
      risks associated with Your exercise of permissions under this License.

   8. Limitation of Liability. In no event and under no legal theory,
      whether in tort (including negligence), contract, or otherwise,
      unless required by applicable law (such as deliberate and grossly
      negligent acts) or agreed to in writing, shall any Contributor be
      liable to You for damages, including any direct, indirect, special,
      incidental, or consequential damages of any character arising as a
      result of this License or out of the use or inability to use the
      Work (including but not limited to damages for loss of goodwill,
      work stoppage, computer failure or malfunction, or any and all
      other commercial damages or losses), even if such Contributor
      has been advised of the possibility of such damages.

   9. Accepting Warranty or Additional Liability. While redistributing
      the Work or Derivative Works thereof, You may choose to offer,
      and charge a fee for, acceptance of support, warranty, indemnity,
      or other liability obligations and/or rights consistent with this
      License. However, in accepting such obligations, You may act only
      on Your own behalf and on Your sole responsibility, not on behalf
      of any other Contributor, and only if You agree to indemnify,
      defend, and hold each Contributor harmless for any liability
      incurred by, or claims asserted against, such Contributor by reason
      of your accepting any such warranty or additional liability.

   END OF TERMS AND CONDITIONS

   APPENDIX: How to apply the Apache License to your work.

      To apply the Apache License to your work, attach the following
      boilerplate notice, with the fields enclosed by brackets "[]"
      replaced with your own identifying information. (Don't include
      the brackets!)  The text should be enclosed in the appropriate
      comment syntax for the file format. We also recommend that a
      file or class name and description of purpose be included on the
      same "printed page" as the copyright notice for easier
      identification within third-party archives.

   Copyright [yyyy] [name of copyright owner]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

grammar Assembler;

prog: (line)* ;

line: statement NEWLINE
    | NEWLINE
    ;

statement: labeledCommand
         | assignment
         ;

labeledCommand: ID? command;

command: DIRECTIVE args?     # DirectiveCommand
       | OPCODE operand?     # OpCommand
       ;

operand: immediateAddr
       | directAddr
       | indexedDirectAddrX
       | indexedDirectAddrY
       | indirectAddr
       | preIndexedIndirectAddr
       | postIndexedIndirectAddr
       ;

immediateAddr: '#' expr ;
directAddr: expr ;
indexedDirectAddrX: expr ',' 'X' ;
indexedDirectAddrY: expr ',' 'Y' ;
indirectAddr: '(' expr ')' ;
preIndexedIndirectAddr: '(' expr ',' 'X' ')' ;
postIndexedIndirectAddr: '(' expr ')' ',' 'Y' ;

args: STRING                # StringArg
    | expr (',' expr)*      # ListArg
    ;

assignment: ID '=' expr     # VarAssign
          | '**' '=' expr   # PcAssign
          ;

expr: ID                    # Var
    | '**'                  # Pc
    | expr '*' expr         # Mult
    | expr '/' expr         # Div
    | expr '\\' expr        # Rem
    | expr '+' expr         # Add
    | expr '-' expr         # Sub
    | number                # Num
    | CHAR                  # Char
    | '[' expr ']'          # Parens
    | '<' expr              # LowByte
    | '>' expr              # HighByte
    ;

number: HEX
      | OCT
      | BIN
      | INT
      ;

STRING: '"' ~["]* '"' ;
INT: DIGIT+ ;
HEX: '$' [0-9a-fA-F]+ ;
OCT: '@' [0-7]+ ;
BIN: '%' [01]+ ;
CHAR: '\'' . ;
OPCODE: ADC  | AND  | ASL  | BBR0 | BBR1 | BBR2 | BBR3 | BBR4 | BBR5 | BBR6
      | BBR7 | BBS0 | BBS1 | BBS2 | BBS3 | BBS4 | BBS5 | BBS6 | BBS7 | BCC
      | BCS  | BEQ  | BIT  | BMI  | BNE  | BPL  | BRA  | BRK  | BVC  | BVS
      | CLC  | CLD  | CLI  | CLV  | CMP  | CPX  | CPY  | DEC  | DEX  | DEY
      | EOR  | INC  | INX  | INY  | JMP  | JSR  | LDA  | LDX  | LDY  | LSR
      | NOP  | ORA  | PHA  | PHP  | PHX  | PHY  | PLA  | PLP  | PLX  | PLY
      | RMB0 | RMB1 | RMB2 | RMB3 | RMB4 | RMB5 | RMB6 | RMB7 | ROL  | ROR
      | RTI  | RTS  | SBC  | SEC  | SED  | SEI  | SMB0 | SMB1 | SMB2 | SMB3
      | SMB4 | SMB5 | SMB6 | SMB7 | STA  | STP  | STX  | STY  | STZ  | TAX
      | TAY  | TRB  | TSB  | TSX  | TXA  | TXS  | TYA  | WAI ;
DIRECTIVE: '.' ID ;
ID: LETTER (LETTER|DIGIT)* ;
WS: [ \t]+ -> skip ;
fragment LETTER: [a-zA-Z] ;
fragment DIGIT: [0-9] ;
NEWLINE: '\r'? '\n' ;
COMMENT: ';' ~[\r\n]* -> skip ;

fragment ADC: 'ADC' ;
fragment AND: 'AND' ;
fragment ASL: 'ASL' ;
fragment BBR0: 'BBR0' ;
fragment BBR1: 'BBR1' ;
fragment BBR2: 'BBR2' ;
fragment BBR3: 'BBR3' ;
fragment BBR4: 'BBR4' ;
fragment BBR5: 'BBR5' ;
fragment BBR6: 'BBR6' ;
fragment BBR7: 'BBR7' ;
fragment BBS0: 'BBS0' ;
fragment BBS1: 'BBS1' ;
fragment BBS2: 'BBS2' ;
fragment BBS3: 'BBS3' ;
fragment BBS4: 'BBS4' ;
fragment BBS5: 'BBS5' ;
fragment BBS6: 'BBS6' ;
fragment BBS7: 'BBS7' ;
fragment BCC: 'BCC' ;
fragment BCS: 'BCS' ;
fragment BEQ: 'BEQ' ;
fragment BIT: 'BIT' ;
fragment BMI: 'BMI' ;
fragment BNE: 'BNE' ;
fragment BPL: 'BPL' ;
fragment BRA: 'BRA' ;
fragment BRK: 'BRK' ;
fragment BVC: 'BVC' ;
fragment BVS: 'BVS' ;
fragment CLC: 'CLC' ;
fragment CLD: 'CLD' ;
fragment CLI: 'CLI' ;
fragment CLV: 'CLV' ;
fragment CMP: 'CMP' ;
fragment CPX: 'CPX' ;
fragment CPY: 'CPY' ;
fragment DEC: 'DEC' ;
fragment DEX: 'DEX' ;
fragment DEY: 'DEY' ;
fragment EOR: 'EOR' ;
fragment INC: 'INC' ;
fragment INX: 'INX' ;
fragment INY: 'INY' ;
fragment JMP: 'JMP' ;
fragment JSR: 'JSR' ;
fragment LDA: 'LDA' ;
fragment LDX: 'LDX' ;
fragment LDY: 'LDY' ;
fragment LSR: 'LSR' ;
fragment NOP: 'NOP' ;
fragment ORA: 'ORA' ;
fragment PHA: 'PHA' ;
fragment PHP: 'PHP' ;
fragment PHX: 'PHX' ;
fragment PHY: 'PHY' ;
fragment PLA: 'PLA' ;
fragment PLP: 'PLP' ;
fragment PLX: 'PLX' ;
fragment PLY: 'PLY' ;
fragment RMB0: 'RMB0' ;
fragment RMB1: 'RMB1' ;
fragment RMB2: 'RMB2' ;
fragment RMB3: 'RMB3' ;
fragment RMB4: 'RMB4' ;
fragment RMB5: 'RMB5' ;
fragment RMB6: 'RMB6' ;
fragment RMB7: 'RMB7' ;
fragment ROL: 'ROL' ;
fragment ROR: 'ROR' ;
fragment RTI: 'RTI' ;
fragment RTS: 'RTS' ;
fragment SBC: 'SBC' ;
fragment SEC: 'SEC' ;
fragment SED: 'SED' ;
fragment SEI: 'SEI' ;
fragment SMB0: 'SMB0' ;
fragment SMB1: 'SMB1' ;
fragment SMB2: 'SMB2' ;
fragment SMB3: 'SMB3' ;
fragment SMB4: 'SMB4' ;
fragment SMB5: 'SMB5' ;
fragment SMB6: 'SMB6' ;
fragment SMB7: 'SMB7' ;
fragment STA: 'STA' ;
fragment STP: 'STP' ;
fragment STX: 'STX' ;
fragment STY: 'STY' ;
fragment STZ: 'STZ' ;
fragment TAX: 'TAX' ;
fragment TAY: 'TAY' ;
fragment TRB: 'TRB' ;
fragment TSB: 'TSB' ;
fragment TSX: 'TSX' ;
fragment TXA: 'TXA' ;
fragment TXS: 'TXS' ;
fragment TYA: 'TYA' ;
fragment WAI: 'WAI' ;