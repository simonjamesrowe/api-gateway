package com.simonjamesrowe.apigateway.entrypoints.restcontroller

interface IContactUsController {

  suspend fun contactUs(
    contactUs: ContactUsController.ContactUs,
    referer: String?
  )
}
