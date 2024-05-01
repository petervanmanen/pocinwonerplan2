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

describe('Ontwikkelwens e2e test', () => {
  const ontwikkelwensPageUrl = '/ontwikkelwens';
  const ontwikkelwensPageUrlPattern = new RegExp('/ontwikkelwens(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ontwikkelwensSample = {};

  let ontwikkelwens;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ontwikkelwens+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ontwikkelwens').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ontwikkelwens/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ontwikkelwens) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ontwikkelwens/${ontwikkelwens.id}`,
      }).then(() => {
        ontwikkelwens = undefined;
      });
    }
  });

  it('Ontwikkelwens menu should load Ontwikkelwens page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ontwikkelwens');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Ontwikkelwens').should('exist');
    cy.url().should('match', ontwikkelwensPageUrlPattern);
  });

  describe('Ontwikkelwens page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ontwikkelwensPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Ontwikkelwens page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ontwikkelwens/new$'));
        cy.getEntityCreateUpdateHeading('Ontwikkelwens');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ontwikkelwensPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ontwikkelwens',
          body: ontwikkelwensSample,
        }).then(({ body }) => {
          ontwikkelwens = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ontwikkelwens+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [ontwikkelwens],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ontwikkelwensPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Ontwikkelwens page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ontwikkelwens');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ontwikkelwensPageUrlPattern);
      });

      it('edit button click should load edit Ontwikkelwens page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ontwikkelwens');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ontwikkelwensPageUrlPattern);
      });

      it('edit button click should load edit Ontwikkelwens page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ontwikkelwens');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ontwikkelwensPageUrlPattern);
      });

      it('last delete button click should delete instance of Ontwikkelwens', () => {
        cy.intercept('GET', '/api/ontwikkelwens/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('ontwikkelwens').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ontwikkelwensPageUrlPattern);

        ontwikkelwens = undefined;
      });
    });
  });

  describe('new Ontwikkelwens page', () => {
    beforeEach(() => {
      cy.visit(`${ontwikkelwensPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Ontwikkelwens');
    });

    it('should create an instance of Ontwikkelwens', () => {
      cy.get(`[data-cy="code"]`).type('14101');
      cy.get(`[data-cy="code"]`).should('have.value', '14101');

      cy.get(`[data-cy="naam"]`).type('system');
      cy.get(`[data-cy="naam"]`).should('have.value', 'system');

      cy.get(`[data-cy="omschrijving"]`).type('fault important');
      cy.get(`[data-cy="omschrijving"]`).should('have.value', 'fault important');

      cy.get(`[data-cy="actief"]`).should('not.be.checked');
      cy.get(`[data-cy="actief"]`).click();
      cy.get(`[data-cy="actief"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        ontwikkelwens = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', ontwikkelwensPageUrlPattern);
    });
  });
});
