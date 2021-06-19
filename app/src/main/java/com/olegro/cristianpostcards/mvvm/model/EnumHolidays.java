package com.olegro.cristianpostcards.mvvm.model;

import com.olegro.cristianpostcards.R;

public enum EnumHolidays {
    BIRTHDAY(1, R.color.colorBirthday, R.drawable.ic_birth_day, "День рождения"),
    DAILY(9, R.color.colorDaily, R.drawable.ic_daily, "Повседневные"),
    CHRISTIAN(11, R.color.colorChristian, R.drawable.ic_christian, "Христианские"),
    CHILDREN(10, R.color.colorChildren, R.drawable.ic_children, "Детские"),
    NINTHMAY(4, R.color.colorNinthMay, R.drawable.ic_ninth_may, "9 Мая"),
    MOTHERSDAY(8, R.color.colorMothersDay, R.drawable.ic_mothers_day, "День матери"),
    CHRISTMAS(7, R.color.colorChristmas, R.drawable.ic_christmas, "Рождество Христово"),
    NEWYEAR(0, R.color.colorNewYear, R.drawable.ic_new_year, "Новый год"),
    VALENTINESDAY(5, R.color.colorValentinesDay, R.drawable.ic_valentines_day, "День влюблённых"),
    EIGHTHMARCH(6, R.color.colorEighthMarch, R.drawable.ic_eight_march, "8 марта"),
    EASTER(2, R.color.colorEaster, R.drawable.ic_easter, "Пасха"),
    FIRSTMAY(3, R.color.colorFirstMay, R.drawable.ic_first_may, "1 Мая");

//    CHRISTIAN(0, 0, 0, "Христианские"),
//    CHILDREN(0, 0, 0, "Детские");

    public final int id;
    public final int color;
    public final int iconID;
    public final String holidayName;

    EnumHolidays(int id, int color, int iconID, String holidayName) {
        this.id = id;
        this.color = color;
        this.iconID = iconID;
        this.holidayName = holidayName;
    }
}
