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

describe('InwonerplanActiviteit e2e test', () => {
  const inwonerplanActiviteitPageUrl = '/inwonerplan-activiteit';
  const inwonerplanActiviteitPageUrlPattern = new RegExp('/inwonerplan-activiteit(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const inwonerplanActiviteitSample = {};

  let inwonerplanActiviteit;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/inwonerplan-activiteits+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/inwonerplan-activiteits').as('postEntityRequest');
    cy.intercept('DELETE', '/api/inwonerplan-activiteits/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (inwonerplanActiviteit) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/inwonerplan-activiteits/${inwonerplanActiviteit.id}`,
      }).then(() => {
        inwonerplanActiviteit = undefined;
      });
    }
  });

  it('InwonerplanActiviteits menu should load InwonerplanActiviteits page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('inwonerplan-activiteit');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InwonerplanActiviteit').should('exist');
    cy.url().should('match', inwonerplanActiviteitPageUrlPattern);
  });

  describe('InwonerplanActiviteit page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inwonerplanActiviteitPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InwonerplanActiviteit page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/inwonerplan-activiteit/new$'));
        cy.getEntityCreateUpdateHeading('InwonerplanActiviteit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inwonerplanActiviteitPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/inwonerplan-activiteits',
          body: inwonerplanActiviteitSample,
        }).then(({ body }) => {
          inwonerplanActiviteit = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/inwonerplan-activiteits+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [inwonerplanActiviteit],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(inwonerplanActiviteitPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InwonerplanActiviteit page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inwonerplanActiviteit');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inwonerplanActiviteitPageUrlPattern);
      });

      it('edit button click should load edit InwonerplanActiviteit page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InwonerplanActiviteit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inwonerplanActiviteitPageUrlPattern);
      });

      it('edit button click should load edit InwonerplanActiviteit page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InwonerplanActiviteit');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inwonerplanActiviteitPageUrlPattern);
      });

      it('last delete button click should delete instance of InwonerplanActiviteit', () => {
        cy.intercept('GET', '/api/inwonerplan-activiteits/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('inwonerplanActiviteit').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inwonerplanActiviteitPageUrlPattern);

        inwonerplanActiviteit = undefined;
      });
    });
  });

  describe('new InwonerplanActiviteit page', () => {
    beforeEach(() => {
      cy.visit(`${inwonerplanActiviteitPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InwonerplanActiviteit');
    });

    it('should create an instance of InwonerplanActiviteit', () => {
      cy.get(`[data-cy="naam"]`).type('till');
      cy.get(`[data-cy="naam"]`).should('have.value', 'till');

      cy.get(`[data-cy="actief"]`).should('not.be.checked');
      cy.get(`[data-cy="actief"]`).click();
      cy.get(`[data-cy="actief"]`).should('be.checked');

      cy.get(`[data-cy="afhandeltermijn"]`).type('9820');
      cy.get(`[data-cy="afhandeltermijn"]`).should('have.value', '9820');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        inwonerplanActiviteit = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', inwonerplanActiviteitPageUrlPattern);
    });
  });
});
