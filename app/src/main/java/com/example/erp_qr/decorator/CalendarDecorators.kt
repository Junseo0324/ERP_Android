package com.example.erp_qr.decorator

import android.content.Context
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.example.erp_qr.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

object CalendarDecorators {

    fun dayDecorator(context: Context): DayViewDecorator {
        return object : DayViewDecorator {
            private val drawable = ContextCompat.getDrawable(context, R.drawable.calendar_selector)
            override fun shouldDecorate(day: CalendarDay): Boolean = true
            override fun decorate(view: DayViewFacade) {
                view.setSelectionDrawable(drawable!!)
            }
        }
    }

    fun todayDecorator(context: Context): DayViewDecorator {
        return object : DayViewDecorator {
            private val backgroundDrawable =
                ContextCompat.getDrawable(context, R.drawable.calendar_circle_today)
            private val today = CalendarDay.today()

            override fun shouldDecorate(day: CalendarDay?): Boolean = day == today

            override fun decorate(view: DayViewFacade?) {
                view?.apply {
                    setBackgroundDrawable(backgroundDrawable!!)
                    addSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                context,
                                R.color.black
                            )
                        )
                    )
                }
            }
        }
    }

    fun rangeDecorator(context: Context,startDate: CalendarDay, endDate: CalendarDay): DayViewDecorator {
        return object : DayViewDecorator {
            private val backgroundDrawable =
                ContextCompat.getDrawable(context, R.drawable.range_background)
            override fun shouldDecorate(day: CalendarDay): Boolean {
                return (day.date >= startDate.date && day.date <= endDate.date)
            }
            override fun decorate(view: DayViewFacade) {
                backgroundDrawable?.let { view.setBackgroundDrawable(it) }
            }
        }
    }

    fun startAndEndDateDecorator(context: Context, date: CalendarDay,isStart: Boolean): DayViewDecorator{
        return object : DayViewDecorator {
            private val spanColor = if (isStart) {
                ContextCompat.getColor(context, R.color.select_date_color)
            } else {
                ContextCompat.getColor(context, R.color.select_date_color)
            }
            override fun shouldDecorate(day: CalendarDay): Boolean {
                return day == date
            }
            override fun decorate(view: DayViewFacade) {
                view.addSpan(ForegroundColorSpan(spanColor)) // 텍스트 색상 변경
                view.addSpan(object :
                    android.text.style.StyleSpan(android.graphics.Typeface.BOLD) {}) // 굵게 표시
            }
        }
    }
}