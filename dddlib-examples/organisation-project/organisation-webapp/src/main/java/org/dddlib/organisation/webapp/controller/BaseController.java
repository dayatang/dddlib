package org.dddlib.organisation.webapp.controller;

import org.dddlib.organisation.facade.OrganisationFacade;

import javax.inject.Inject;

/**
 * Created by yyang on 14-8-12.
 */
public class BaseController {

    @Inject
    protected OrganisationFacade facade;
}
