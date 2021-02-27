package com.simonjamesrowe.apigateway.service

import com.itextpdf.io.font.FontProgram
import com.itextpdf.io.font.FontProgramFactory
import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.kernel.colors.WebColors
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.action.PdfAction
import com.itextpdf.layout.Document
import com.itextpdf.layout.Style
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.*
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import com.simonjamesrowe.model.data.Job
import com.simonjamesrowe.model.data.Profile
import com.simonjamesrowe.model.data.Skill
import com.simonjamesrowe.model.data.SocialMedia
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.time.format.DateTimeFormatter


@Service
class ResumeInteractor(
  private val resumeRepository: ResumeRepository
) {

  val fontProgram: FontProgram = FontProgramFactory.createFont(ClassPathResource("OpenSans-Regular.ttf").inputStream.readAllBytes())

  fun toPdf(): ByteArray {
    val font = PdfFontFactory.createFont(fontProgram, PdfEncodings.UTF8, true)
    val data = resumeRepository.getResumeData()
    val byteArrayOutputStream = ByteArrayOutputStream()
    val writer = PdfWriter(byteArrayOutputStream)
    val pdfDoc = PdfDocument(writer)
    pdfDoc.addNewPage()
    val document = Document(pdfDoc)
    document.setFont(font)
    addSideBar(document, data.profile, data.skills, data.socialMedias)
    addExperience(document, data.jobs, data.education)
    addHeadline(document, data.profile)
    pdfDoc.close()
    document.close()
    return byteArrayOutputStream.toByteArray()
  }

  private fun addExperience(document: Document, jobs: List<Job>, education: Job?) {
    val div = Div().also {
      it.height = UnitValue.createPointValue(842f)
      it.width = UnitValue.createPointValue(380f)
      it.setFixedPosition(
        176f,
        0f,
        UnitValue.createPointValue(380f)
      )
    }
    div.add(blank())
    div.add(employmentHeading("Employment History"))
    jobs.sortedByDescending { it.startDate }.forEach { job -> div.add(jobBlock(job)) }
    if (education != null) {
      div.add(employmentHeading("Education"))
      div.add(jobBlock(education))
    }
    document.add(div)
  }

  private val FONT_7_PT = 7f
  private val FONT_9_PT = 9f
  private val FONT_8_PT = 8f

  private fun jobBlock(job: Job): Div {
    var jobDiv = Div().also {
      it.setMarginBottom(10f)
      it.setMarginRight(20f)
      it.setMarginLeft(10f)
    }
    var table = Table(2).also {
      it.width = UnitValue.createPercentValue(100f)
      it.setBorder(Border.NO_BORDER)
      it.addCell(Cell().also {
        it.width = UnitValue.createPercentValue(80f)
        it.add(
          Paragraph(
            Link(
              "${job.title}, ${job.company}",
              PdfAction.createURI("https://www.simonjamesrowe.com/jobs/${job.id}")
            )
          )
        )
        it.setBorder(Border.NO_BORDER)
        it.setFontSize(FONT_9_PT)
        it.setBold()

      })
      it.addCell(Cell().also {
        it.width = UnitValue.createPercentValue(20f)
        it.setTextAlignment(TextAlignment.RIGHT)
        it.add(Paragraph(job.location))
        it.setBorder(Border.NO_BORDER)
        it.setFontSize(FONT_7_PT)
      })
    }
    jobDiv.add(table)
    jobDiv.add(
      Paragraph(
        "${job.startDate.format(DateTimeFormatter.ofPattern("MMM-yyyy"))} to ${
          job.endDate?.format(
            DateTimeFormatter.ofPattern(
              "MMM-yyyy"
            )
          ) ?: "Present"
        }"
      ).also {
        it.setFontSize(FONT_8_PT)
        it.setPadding(0f)
        it.setMarginTop(0f)
        it.setMarginLeft(2f)
      }
    )
    jobDiv.add(
      Paragraph(job.shortDescription).also {
        it.setFontSize(FONT_8_PT)
        it.setPadding(0f)
        it.setMarginTop(0f)
        it.setMarginLeft(2f)
      }
    )
    return jobDiv
  }

  private fun addSideBar(
    document: Document,
    profile: Profile,
    skills: List<Skill>,
    socialMedias: List<SocialMedia>
  ) {
    val div = Div().also {
      it.height = UnitValue.createPointValue(842f)
      it.width = UnitValue.createPointValue(175f)
      it.setBackgroundColor(WebColors.getRGBColor("#dddddd"))
      it.setFixedPosition(
        0f,
        0f,
        UnitValue.createPointValue(175f)
      )
    }
    div.add(blank())
    div.add(sidebarHeading("INFO"))
    div.add(sidebarContactInfo("phone", profile.phoneNumber))
    div.add(sidebarContactInfo("email", profile.primaryEmail))
    div.add(sidebarHeading("Links"))
    div.add(link("https://www.simonjamesrowe.com"))
    socialMedias.forEach { div.add(link(it.link)) }
    div.add(sidebarHeading("Skills"))
    skills.forEach { div.add(skill(it)) }
    document.add(div)
  }

  private val SKILLS_MARGIN_LEFT = 35f

  private fun skill(skill: Skill) = Div().also {
    it.setMarginLeft(SKILLS_MARGIN_LEFT)
    it.add(Paragraph(skill.name).also {
      it.setFontSize(FONT_7_PT)
      it.setPaddingTop(0f)
      it.setPaddingBottom(0f)
      it.setMarginTop(0f)
      it.setMarginBottom(0f)
    })

    it.add(Paragraph(stars(skill.rating)).also {
      it.setPaddingTop(0f)
      it.setBold()
      it.setFontSize(10f)
      it.setPaddingBottom(0f)
    })
    it.setMarginBottom(0f)
  }

  private fun stars(rating: Double): String{
    return (0 until ((rating).toInt())).joinToString("") { "* " }
  }

  private fun link(url: String) =
    Paragraph(
      Link(
        url,
        PdfAction.createURI(url)
      )
    ).also {
      it.setMarginLeft(SKILLS_MARGIN_LEFT)
      it.setMarginRight(10f)
      it.setFontSize(FONT_7_PT)
    }


  private fun blank(top: Float = 160f) =
    Div().also {
      it.setMarginTop(top)
    }

  private fun sidebarHeading(heading: String) =
    Div().also {
      it.setMarginRight(20f)
      it.setMarginLeft(SKILLS_MARGIN_LEFT)
      it.setMarginBottom(5f)
      it.add(Paragraph(heading.toUpperCase()))
      it.setBorderBottom(SolidBorder(1f))
      it.setFontSize(10f)
    }

  private fun employmentHeading(heading: String) =
    Div().also {
      it.setMarginRight(20f)
      it.setMarginLeft(10f)
      it.setMarginBottom(10f)
      it.add(Paragraph(heading.toUpperCase()))
      it.setBorderBottom(SolidBorder(1f))
      it.setFontSize(10f)
    }

  private fun sidebarContactInfo(label: String, value: String) =
    Div().also {
      it.setMarginRight(20f)
      it.setMarginLeft(SKILLS_MARGIN_LEFT)
      it.setMarginBottom(10f)
      it.add(
        Paragraph(label.toUpperCase()).also {
          it.setBold()
          it.setFontSize(FONT_7_PT)
          it.setPadding(0f)
          it.setMargin(0f)
        })
      it.add(Paragraph(value).also {
        it.setFontSize(FONT_7_PT)
        it.setPadding(0f)
        it.setMargin(0f)
      })
    }


  private fun addHeadline(document: Document, profile: Profile) {
    val div = Div().also {
      it.setBorder(SolidBorder(1.5f))
      it.width = UnitValue.createPercentValue(75.0f)
      it.setPadding(20f)
      it.setBackgroundColor(WebColors.getRGBColor("#ffffff"))
      it.setFixedPosition(
        130f,
        document.getPageEffectiveArea(PageSize.A4).height - 50,
        UnitValue.createPercentValue(75.0f)
      )

    }
    val nameParagraph = Paragraph(profile.name.toUpperCase()).also {
      it.addStyle(Style().also {
        it.setTextAlignment(TextAlignment.CENTER)
        it.setBold()
        it.setFontSize(20f)
      })
    }
    val headlineParagraph = Paragraph(profile.headline.toUpperCase()).also {
      it.addStyle(Style().also {
        it.setTextAlignment(TextAlignment.CENTER)
        it.setFontSize(11f)
      })
    }
    div.add(nameParagraph)
    div.add(headlineParagraph)
    document.add(div)
  }

}
