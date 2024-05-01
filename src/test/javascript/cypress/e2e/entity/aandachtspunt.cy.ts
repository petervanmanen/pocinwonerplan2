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

describe('Aandachtspunt e2e test', () => {
  const aandachtspuntPageUrl = '/aandachtspunt';
  const aandachtspuntPageUrlPattern = new RegExp('/aandachtspunt(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const aandachtspuntSample = {};

  let aandachtspunt;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/aandachtspunts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/aandachtspunts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/aandachtspunts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (aandachtspunt) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/aandachtspunts/${aandachtspunt.id}`,
      }).then(() => {
        aandachtspunt = undefined;
      });
    }
  });

  it('Aandachtspunts menu should load Aandachtspunts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('aandachtspunt');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Aandachtspunt').should('exist');
    cy.url().should('match', aandachtspuntPageUrlPattern);
  });

  describe('Aandachtspunt page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(aandachtspuntPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Aandachtspunt page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/aandachtspunt/new$'));
        cy.getEntityCreateUpdateHeading('Aandachtspunt');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aandachtspuntPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/aandachtspunts',
          body: aandachtspuntSample,
        }).then(({ body }) => {
          aandachtspunt = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/aandachtspunts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [aandachtspunt],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(aandachtspuntPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Aandachtspunt page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('aandachtspunt');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aandachtspuntPageUrlPattern);
      });

      it('edit button click should load edit Aandachtspunt page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Aandachtspunt');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aandachtspuntPageUrlPattern);
      });

      it('edit button click should load edit Aandachtspunt page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Aandachtspunt');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aandachtspuntPageUrlPattern);
      });

      it('last delete button click should delete instance of Aandachtspunt', () => {
        cy.intercept('GET', '/api/aandachtspunts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('aandachtspunt').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aandachtspuntPageUrlPattern);

        aandachtspunt = undefined;
      });
    });
  });

  describe('new Aandachtspunt page', () => {
    beforeEach(() => {
      cy.visit(`${aandachtspuntPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Aandachtspunt');
    });

    it('should create an instance of Aandachtspunt', () => {
      cy.get(`[data-cy="code"]`).type('28612');
      cy.get(`[data-cy="code"]`).should('have.value', '28612');

      cy.get(`[data-cy="naam"]`).type('construe');
      cy.get(`[data-cy="naam"]`).should('have.value', 'construe');

      cy.get(`[data-cy="omschrijving"]`).type('spawn how up');
      cy.get(`[data-cy="omschrijving"]`).should('have.value', 'spawn how up');

      cy.get(`[data-cy="actief"]`).should('not.be.checked');
      cy.get(`[data-cy="actief"]`).click();
      cy.get(`[data-cy="actief"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        aandachtspunt = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', aandachtspuntPageUrlPattern);
    });
  });
});
