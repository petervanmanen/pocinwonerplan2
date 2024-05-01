import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Inwonerplan e2e test', () => {
  const inwonerplanPageUrl = '/inwonerplan';
  const inwonerplanPageUrlPattern = new RegExp('/inwonerplan(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const inwonerplanSample = { bsn: 'nearer successfully repository' };

  let inwonerplan;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/inwonerplans+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/inwonerplans').as('postEntityRequest');
    cy.intercept('DELETE', '/api/inwonerplans/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (inwonerplan) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/inwonerplans/${inwonerplan.id}`,
      }).then(() => {
        inwonerplan = undefined;
      });
    }
  });

  it('Inwonerplans menu should load Inwonerplans page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('inwonerplan');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Inwonerplan').should('exist');
    cy.url().should('match', inwonerplanPageUrlPattern);
  });

  describe('Inwonerplan page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inwonerplanPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Inwonerplan page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/inwonerplan/new$'));
        cy.getEntityCreateUpdateHeading('Inwonerplan');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inwonerplanPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/inwonerplans',
          body: inwonerplanSample,
        }).then(({ body }) => {
          inwonerplan = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/inwonerplans+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [inwonerplan],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(inwonerplanPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Inwonerplan page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inwonerplan');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inwonerplanPageUrlPattern);
      });

      it('edit button click should load edit Inwonerplan page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Inwonerplan');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inwonerplanPageUrlPattern);
      });

      it('edit button click should load edit Inwonerplan page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Inwonerplan');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inwonerplanPageUrlPattern);
      });

      it('last delete button click should delete instance of Inwonerplan', () => {
        cy.intercept('GET', '/api/inwonerplans/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('inwonerplan').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inwonerplanPageUrlPattern);

        inwonerplan = undefined;
      });
    });
  });

  describe('new Inwonerplan page', () => {
    beforeEach(() => {
      cy.visit(`${inwonerplanPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Inwonerplan');
    });

    it('should create an instance of Inwonerplan', () => {
      cy.get(`[data-cy="bsn"]`).type('perfect paltry');
      cy.get(`[data-cy="bsn"]`).should('have.value', 'perfect paltry');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        inwonerplan = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', inwonerplanPageUrlPattern);
    });
  });
});
