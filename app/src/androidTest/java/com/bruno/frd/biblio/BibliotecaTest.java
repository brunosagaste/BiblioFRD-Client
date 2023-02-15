package com.bruno.frd.biblio;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Checks.checkNotNull;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.bruno.frd.biblio.CardMatcher.atPosition;
import static com.bruno.frd.biblio.CustomTestMatchers.withBackgroundColour;
import static com.bruno.frd.biblio.IndexMatcher.withIndex;
import static com.bruno.frd.biblio.PairMatcher.withPairValue;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import com.bruno.frd.biblio.ui.MainActivity;

import java.io.IOException;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BibliotecaTest {

    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Before
    public void intentsInit() {
        // initialize Espresso Intents capturing
        Intents.init();
    }

    @After
    public void intentsTeardown() {
        // release Espresso Intents capturing
        Intents.release();
    }

    @Test
    public void PGP01a() {
        // Verificar que se muestre "No tiene préstamos vigentes"
        onView(withId(R.id.text_empty_state)).check(matches(withText("No tiene préstamos vigentes")));
    }

    @Test
    public void PGP01b() {
        // Primera tarjeta
        // Verificar que se muestre "Vencido"
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(withText("Vencido")), 0)));
        // Verificar que no se muestre el botón de renovar
        onView(withIndex(withText("Renovar"), 0)).check(matches(not(isDisplayed())));
        // Verificar que el color del indicador es rojo
        onView(withIndex(withId(R.id.indicator_status), 0)).check(matches(withBackgroundColour(R.color.overdue)));
        // Segunda tarjeta
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(withText("Vencido")), 1)));
        onView(withIndex(withText("Renovar"), 1)).check(matches(not(isDisplayed())));
        onView(withIndex(withId(R.id.indicator_status), 1)).check(matches(withBackgroundColour(R.color.overdue)));
    }

    @Test
    public void PGP01c() {
        // Primera tarjeta
        // Verificar que se muestre "No renovable"
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(withText("No renovable")), 0)));
        // Verificar que no se muestre el botón de renovar
        onView(withIndex(withText("Renovar"), 0)).check(matches(not(isDisplayed())));
        // Verificar que el color del indicador es verde
        onView(withIndex(withId(R.id.indicator_status), 0)).check(matches(withBackgroundColour(R.color.notoverdue)));
        // Segunda tarjeta
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(withText("Vencido")), 1)));
        onView(withIndex(withText("Renovar"), 1)).check(matches(not(isDisplayed())));
        onView(withIndex(withId(R.id.indicator_status), 1)).check(matches(withBackgroundColour(R.color.overdue)));
        // Tercera tarjeta
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(withText("Vencido")), 2)));
        onView(withIndex(withText("Renovar"), 2)).check(matches(not(isDisplayed())));
        onView(withIndex(withId(R.id.indicator_status), 2)).check(matches(withBackgroundColour(R.color.overdue)));
        // Cuarta tarjeta
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(withText("Vencido")), 3)));
        onView(withIndex(withText("Renovar"), 3)).check(matches(not(isDisplayed())));
        onView(withIndex(withId(R.id.indicator_status), 3)).check(matches(withBackgroundColour(R.color.overdue)));
        // Quinta tarjeta
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(withText("Vencido")), 4)));
        onView(withIndex(withText("Renovar"), 4)).check(matches(not(isDisplayed())));
        onView(withIndex(withId(R.id.indicator_status), 4)).check(matches(withBackgroundColour(R.color.overdue)));
        // Sexta tarjeta
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(withText("Vencido")), 5)));
        onView(withIndex(withText("Renovar"), 5)).check(matches(not(isDisplayed())));
        onView(withIndex(withId(R.id.indicator_status), 5)).check(matches(withBackgroundColour(R.color.overdue)));
    }

    @Test
    public void PGP01d() {
        // Primera tarjeta
        // Verificar que se muestre el botón de renovar
        onView(withIndex(withText("Renovar"), 0)).check(matches(isDisplayed()));
        // Verificar que el color del indicador es verde
        onView(withIndex(withId(R.id.indicator_status), 0)).check(matches(withBackgroundColour(R.color.notoverdue)));
        // Segunda tarjeta
        onView(withIndex(withText("Renovar"), 1)).check(matches(isDisplayed()));
        onView(withIndex(withId(R.id.indicator_status), 1)).check(matches(withBackgroundColour(R.color.notoverdue)));
    }

    @Test
    public void PGP02a() throws InterruptedException {
        Thread.sleep(1000);
        // 1
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 0)));
        // 2
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 1)));
        // 3
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 2)));
        // 4
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 3)));
        // 5
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 4)));
        // 6
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 5)));
        // 7
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 6)));
        // Scroll
        onView(withId(R.id.list_loans)).perform(RecyclerViewActions.scrollToPosition(12));
        // 8
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 7)));
        // 9
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 8)));
        // 10
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 9)));
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(withText("Renovable a partir del 17/02")), 9)));
        // 11
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(withText("Renovar")), 10)));
        // 12
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 11)));
        // 13
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 12)));
        // Scroll
        onView(withId(R.id.list_loans)).perform(RecyclerViewActions.scrollToPosition(13));
        // 14
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 13)));
    }

    @Test
    public void PGP02bI() {
        onView(withId(R.id.button_renew)).perform(click());
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 0)));
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(withText("Renovable a partir del 13/02")), 0)));
    }

    @Test
    public void PGP02bII() {
        onView(withId(R.id.button_renew)).perform(click());
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(not(withText("Renovar"))), 0)));
        onView(withId(R.id.list_loans)).check(matches(atPosition(hasDescendant(withText("No renovable")), 0)));
    }

    @Test
    public void PGP03() {
        onView(withId(R.id.list_loans))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText("Biotecnología. Manual de microbiología industrial")),
                        click()));
        // Número de copia
        onView(withId(R.id.item_list)).check(matches(withPairValue("1850", 0)));
        // Autor
        onView(withId(R.id.item_list)).check(matches(withPairValue("Wulf Crueger, Anneliese Crueger", 1)));
        // Estado
        onView(withId(R.id.item_list)).check(matches(withPairValue("No renovable", 2)));
        // Inicio del préstamo
        onView(withId(R.id.item_list)).check(matches(withPairValue("10/02/2023", 3)));
        // Vencimiento
        onView(withId(R.id.item_list)).check(matches(withPairValue("16/02/2023", 4)));
        // Número de libro
        onView(withId(R.id.item_list)).check(matches(withPairValue("455", 5)));
    }

    @Test
    public void PGCP01I() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText("algebra"));
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        // Se prueban los primeros 3 resultados. El resto fue probado en PS-CP-I.
        onView(withId(R.id.list_search)).check(matches(atPosition(hasDescendant(withText(containsString("Álgebra"))), 0)));
        onView(withId(R.id.list_search)).check(matches(atPosition(hasDescendant(withText(containsString("Álgebra"))), 1)));
        onView(withId(R.id.list_search)).check(matches(atPosition(hasDescendant(withText(containsString("Álgebra"))), 2)));
    }

    @Test
    public void PGCP01II() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText("beer"));
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        // Se prueban los primeros 3 resultados. El resto fue probado en PS-CP-II.
        onView(withId(R.id.list_search)).check(matches(atPosition(hasDescendant(withText(containsString("Beer"))), 0)));
        onView(withId(R.id.list_search)).check(matches(atPosition(hasDescendant(withText(containsString("Beer"))), 1)));
        onView(withId(R.id.list_search)).check(matches(atPosition(hasDescendant(withText(containsString("Beer"))), 2)));
    }

    @Test
    public void PGCP02() throws InterruptedException {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText("calculo industrial"));
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        Thread.sleep(1000);
        onView(withIndex((withId(R.id.book_info)), 0)).perform(click());
        // Copias libres
        onView(withId(R.id.item_list)).check(matches(withPairValue("0", 0)));
        // Autor
        onView(withId(R.id.item_list)).check(matches(withPairValue("Juan Corrales Martín", 1)));
        // Tema
        onView(withId(R.id.item_list)).check(matches(withPairValue("Máquinas eléctricas", 2)));
        // Número de libro
        onView(withId(R.id.item_list)).check(matches(withPairValue("1085", 3)));
        // Devolución más cercana
        onView(withId(R.id.item_list)).check(matches(withPairValue("06/09/2022", 4)));
    }

    @Test
    public void PGIP01() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        // Legajo
        onView(withId(R.id.item_list)).check(matches(withPairValue("907599", 0)));
        // DNI
        onView(withId(R.id.item_list)).check(matches(withPairValue("39763633", 1)));
        // Correo
        onView(withId(R.id.item_list)).check(matches(withPairValue("brunosagaste@gmail.com", 2)));
        // Dirección
        onView(withId(R.id.item_list)).check(matches(withPairValue("742 Evergreen Terrace", 3)));
        // Ciudad
        onView(withId(R.id.item_list)).check(matches(withPairValue("Springfield", 4)));
        // Teléfono
        onView(withId(R.id.item_list)).check(matches(withPairValue("232315668346", 5)));
    }

    @Test
    public void PGCC01I() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.oldpw)).perform(typeText("Bruno123*"));
        onView(withId(R.id.new_pw)).perform(typeText("Bruno1234*"));
        onView(withId(R.id.confirm_pw)).perform(typeText("Bruno1234*"));
        onView(withId(R.id.password_change_button)).perform(click());
        onView(withText("Contraseña actualizada con éxito")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGCC01II() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.oldpw)).perform(typeText("Bruno123*"));
        onView(withId(R.id.new_pw)).perform(typeText("Bruno1234*"));
        onView(withId(R.id.confirm_pw)).perform(typeText("Bruno1234*"));
        onView(withId(R.id.password_change_button)).perform(click());
        onView(withText("La contraseña actual no es correcta")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGCC01III() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.oldpw)).perform(typeText("Bruno123*"));
        onView(withId(R.id.new_pw)).perform(typeText("Bruno123"));
        onView(withId(R.id.confirm_pw)).perform(typeText("Bruno123"));
        onView(withId(R.id.password_change_button)).perform(click());
        onView(withText("La contraseña actual no es correcta")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGCC01IV() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.oldpw)).perform(typeText("Bruno123*"));
        onView(withId(R.id.new_pw)).perform(typeText("Bruno123"));
        onView(withId(R.id.confirm_pw)).perform(typeText("Bruno12"));
        onView(withId(R.id.password_change_button)).perform(click());
        onView(withText("La contraseña actual no es correcta")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGCC01V() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.oldpw)).perform(typeText("Bruno1234*"));
        onView(withId(R.id.password_change_button)).perform(click());
        onView(withText("La contraseña no debe ser vacía")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGCC01VI() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.oldpw)).perform(typeText("Bruno1234*"));
        onView(withId(R.id.new_pw)).perform(typeText("Br12*"));
        onView(withId(R.id.confirm_pw)).perform(typeText("Br12*"));
        onView(withId(R.id.password_change_button)).perform(click());
        onView(withText("La contraseña debe tener más de 8 caracteres")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGCC01VII() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.oldpw)).perform(typeText("Bruno1234*"));
        onView(withId(R.id.new_pw)).perform(typeText("Br12"));
        onView(withId(R.id.confirm_pw)).perform(typeText("Br12"));
        onView(withId(R.id.password_change_button)).perform(click());
        onView(withText("La contraseña debe tener más de 8 caracteres")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGCC01VIII() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.oldpw)).perform(typeText("Bruno1234*"));
        onView(withId(R.id.new_pw)).perform(typeText("Bruno123456789123456*"));
        onView(withId(R.id.confirm_pw)).perform(typeText("Bruno123456789123456*"));
        onView(withId(R.id.password_change_button)).perform(click());
        onView(withText("La contraseña debe tener menos de 20 caracteres")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGCC01IX() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.oldpw)).perform(typeText("Bruno1234*"));
        onView(withId(R.id.new_pw)).perform(typeText("Bruno123456789123456*"));
        onView(withId(R.id.confirm_pw)).perform(typeText("Bruno1234567891234567*"));
        onView(withId(R.id.password_change_button)).perform(click());
        onView(withText("La contraseña debe tener menos de 20 caracteres")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGCC01X() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.oldpw)).perform(typeText("Bruno1234*"));
        onView(withId(R.id.new_pw)).perform(typeText("Bruno123"));
        onView(withId(R.id.confirm_pw)).perform(typeText("Bruno123"));
        onView(withId(R.id.password_change_button)).perform(click());
        onView(withText("La contraseña debe tener al menos un caracter especial")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGCC01XI() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.oldpw)).perform(typeText("Bruno1234*"));
        onView(withId(R.id.new_pw)).perform(typeText("Brunoooo*"));
        onView(withId(R.id.confirm_pw)).perform(typeText("Brunoooo*"));
        onView(withId(R.id.password_change_button)).perform(click());
        onView(withText("La contraseña debe contener al menos un número")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGCC01XII() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.oldpw)).perform(typeText("Bruno1234*"));
        onView(withId(R.id.new_pw)).perform(typeText("123456789*"));
        onView(withId(R.id.confirm_pw)).perform(typeText("123456789*"));
        onView(withId(R.id.password_change_button)).perform(click());
        onView(withText("La contraseña debe tener al menos una letra")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGCC01XIII() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.oldpw)).perform(typeText("Bruno1234*"));
        onView(withId(R.id.new_pw)).perform(typeText("bruno123*"));
        onView(withId(R.id.confirm_pw)).perform(typeText("bruno123*"));
        onView(withId(R.id.password_change_button)).perform(click());
        onView(withText("La contraseña debe tener al menos una mayúscula")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGCC02() throws InterruptedException {
        Thread.sleep(3000);
        Espresso.onView(ViewMatchers.withId(R.id.list_loans)).perform(ViewActions.swipeDown());
        Thread.sleep(1000);
        onView(withId(R.id.email_sign_in_button)).check(matches(isDisplayed()));
    }

    @Test
    public void PGLOG01I() {
        onView(withId(R.id.user_id)).perform(typeText("brunosagaste@gmail.com"));
        onView(withId(R.id.password)).perform(typeText("bruno"));
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.list_loans)).check(matches(isDisplayed()));
    }

    @Test
    public void PGLOG01II() {
        onView(withId(R.id.user_id)).perform(typeText("brunosagaste@gmail.com"));
        onView(withId(R.id.password)).perform(typeText("bruno123"));
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withText("Correo, legajo o contraseña incorrecta")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGLOG01III() {
        onView(withId(R.id.user_id)).perform(typeText("brunosagast@gmail.com"));
        onView(withId(R.id.password)).perform(typeText("bruno"));
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withText("Correo, legajo o contraseña incorrecta")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGLOG01IV() {
        onView(withId(R.id.user_id)).perform(typeText("brunosagast@gmail.com"));
        onView(withId(R.id.password)).perform(typeText("bruno12"));
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withText("Correo, legajo o contraseña incorrecta")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGNAV01I() {
        onView(withText("Iniciar sesión")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGNAV01II() {
        onView(withId(R.id.list_loans)).check(matches(isDisplayed()));
    }

    @Test
    public void PGNAV02() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.item_list)).check(matches(isDisplayed()));
        pressBack();
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Cerrar sesión")).perform(click());
        onView(withText("Iniciar sesión")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGNAV03() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.search_src_text)).check(matches(isDisplayed()));
        assertTrue(isKeyboardOpenedShellCheck());
    }

    @Test
    public void PGNAV04() {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Mi perfil")).perform(click());
        onView(withId(R.id.fab)).perform(click());
        assertTrue(isKeyboardOpenedShellCheck());
    }

    @Test
    public void PGER01I() {
        onView(withText("No hay conexión con el servidor")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGER01II() throws InterruptedException {
        Thread.sleep(1000);
        onView(withText("No hay conexión con el servidor")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGER01III() {
        onView(withText("Ha ocurrido un error. Contacte al administrador.")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void PGER02() throws InterruptedException {
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Cerrar sesión")).perform(click());
        Thread.sleep(1000);
        onView(withText("No hay conexión con el servidor")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    private Boolean isKeyboardOpenedShellCheck() {
        String checkKeyboardCmd = "dumpsys input_method | grep mInputShown";
        try {
            return UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                    .executeShellCommand(checkKeyboardCmd).contains("mInputShown=true");
        } catch (IOException e) {
            throw new RuntimeException("Keyboard check failed", e);
        }
    }

}
