/*
 * Scary disclaimer notice - If this software causes significant harm to
 * you pc, mac, laptop, smart phone, dumb phone, tablet, tv, washing machine
 * or dishwasher - which it will - do not blame the author of this software.
 * He spent minutes researching best practices (read poor practices) and copying
 * and pasting lots of dangerous code from the online internet. Hence, this
 * application should not be compiled, executed, distributed, downloaded,
 * sold, purchased, or otherwise used without expressed written consent from
 * the National Football League.
 *
 * Under no circumstances should this program be run as-is, or even as-it-was
 * or will-be. There is no hope for it. You have been warned. Twice now really.
 * I can name one hundred other ways you can spend your time other than running
 * this application. Adopt a dog, walk the dog, feed the dog, pet the dog. I
 * guess what I'm trying to say is that the ITunes EULA actually forbids users
 * from building nuclear weapons if you can believe it:
 *
 * "You also agree that you will not use these products for any purposes
 * prohibited by United States law, including, without limitation, the development,
 * design, manufacture or production of nuclear, missiles, or chemical or
 * biological weapons.”
 */
package com.arturospizzeria;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class PizzeriaRestServicesApplicationTests {

}