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

describe('Activiteit e2e test', () => {
  const activiteitPageUrl = '/activiteit';
  const activiteitPageUrlPattern = new RegExp('/activiteit(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const activiteitSample = {};

  let activiteit;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/activiteits+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/activiteits').as('postEntityRequest');
    cy.intercept('DELETE', '/api/activiteits/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (activiteit) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/activiteits/${activiteit.id}`,
      }).then(() => {
        activiteit = undefined;
      });
    }
  });

  it('Activiteits menu should load Activiteits page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('activiteit');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Activiteit').should('exist');
    cy.url().should('match', activiteitPageUrlPattern);
  });

  describe('Activiteit page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(activiteitPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Activiteit page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/activiteit/new$'));
        cy.getEntityCreateUpdateHeading('Activiteit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activiteitPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/activiteits',
          body: activiteitSample,
        }).then(({ body }) => {
          activiteit = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/activiteits+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [activiteit],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(activiteitPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Activiteit page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('activiteit');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activiteitPageUrlPattern);
      });

      it('edit button click should load edit Activiteit page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Activiteit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activiteitPageUrlPattern);
      });

      it('edit button click should load edit Activiteit page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Activiteit');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activiteitPageUrlPattern);
      });

      it('last delete button click should delete instance of Activiteit', () => {
        cy.intercept('GET', '/api/activiteits/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('activiteit').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activiteitPageUrlPattern);

        activiteit = undefined;
      });
    });
  });

  describe('new Activiteit page', () => {
    beforeEach(() => {
      cy.visit(`${activiteitPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Activiteit');
    });

    it('should create an instance of Activiteit', () => {
      cy.get(`[data-cy="code"]`).type('22033');
      cy.get(`[data-cy="code"]`).should('have.value', '22033');

      cy.get(`[data-cy="naam"]`).type('preference offensively slander');
      cy.get(`[data-cy="naam"]`).should('have.value', 'preference offensively slander');

      cy.get(`[data-cy="actiehouder"]`).type('playfully formal');
      cy.get(`[data-cy="actiehouder"]`).should('have.value', 'playfully formal');

      cy.get(`[data-cy="afhandeltermijn"]`).type('16698');
      cy.get(`[data-cy="afhandeltermijn"]`).should('have.value', '16698');

      cy.get(`[data-cy="actief"]`).should('not.be.checked');
      cy.get(`[data-cy="actief"]`).click();
      cy.get(`[data-cy="actief"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        activiteit = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', activiteitPageUrlPattern);
    });
  });
});
