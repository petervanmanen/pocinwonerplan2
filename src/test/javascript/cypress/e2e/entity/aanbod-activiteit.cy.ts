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

describe('AanbodActiviteit e2e test', () => {
  const aanbodActiviteitPageUrl = '/aanbod-activiteit';
  const aanbodActiviteitPageUrlPattern = new RegExp('/aanbod-activiteit(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const aanbodActiviteitSample = {};

  let aanbodActiviteit;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/aanbod-activiteits+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/aanbod-activiteits').as('postEntityRequest');
    cy.intercept('DELETE', '/api/aanbod-activiteits/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (aanbodActiviteit) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/aanbod-activiteits/${aanbodActiviteit.id}`,
      }).then(() => {
        aanbodActiviteit = undefined;
      });
    }
  });

  it('AanbodActiviteits menu should load AanbodActiviteits page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('aanbod-activiteit');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AanbodActiviteit').should('exist');
    cy.url().should('match', aanbodActiviteitPageUrlPattern);
  });

  describe('AanbodActiviteit page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(aanbodActiviteitPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AanbodActiviteit page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/aanbod-activiteit/new$'));
        cy.getEntityCreateUpdateHeading('AanbodActiviteit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aanbodActiviteitPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/aanbod-activiteits',
          body: aanbodActiviteitSample,
        }).then(({ body }) => {
          aanbodActiviteit = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/aanbod-activiteits+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [aanbodActiviteit],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(aanbodActiviteitPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AanbodActiviteit page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('aanbodActiviteit');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aanbodActiviteitPageUrlPattern);
      });

      it('edit button click should load edit AanbodActiviteit page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AanbodActiviteit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aanbodActiviteitPageUrlPattern);
      });

      it('edit button click should load edit AanbodActiviteit page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AanbodActiviteit');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aanbodActiviteitPageUrlPattern);
      });

      it('last delete button click should delete instance of AanbodActiviteit', () => {
        cy.intercept('GET', '/api/aanbod-activiteits/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('aanbodActiviteit').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aanbodActiviteitPageUrlPattern);

        aanbodActiviteit = undefined;
      });
    });
  });

  describe('new AanbodActiviteit page', () => {
    beforeEach(() => {
      cy.visit(`${aanbodActiviteitPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AanbodActiviteit');
    });

    it('should create an instance of AanbodActiviteit', () => {
      cy.get(`[data-cy="naam"]`).type('plaster ack dairy');
      cy.get(`[data-cy="naam"]`).should('have.value', 'plaster ack dairy');

      cy.get(`[data-cy="actief"]`).should('not.be.checked');
      cy.get(`[data-cy="actief"]`).click();
      cy.get(`[data-cy="actief"]`).should('be.checked');

      cy.get(`[data-cy="afhandeltermijn"]`).type('19198');
      cy.get(`[data-cy="afhandeltermijn"]`).should('have.value', '19198');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        aanbodActiviteit = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', aanbodActiviteitPageUrlPattern);
    });
  });
});
