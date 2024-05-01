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

describe('Aanbod e2e test', () => {
  const aanbodPageUrl = '/aanbod';
  const aanbodPageUrlPattern = new RegExp('/aanbod(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const aanbodSample = {};

  let aanbod;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/aanbods+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/aanbods').as('postEntityRequest');
    cy.intercept('DELETE', '/api/aanbods/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (aanbod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/aanbods/${aanbod.id}`,
      }).then(() => {
        aanbod = undefined;
      });
    }
  });

  it('Aanbods menu should load Aanbods page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('aanbod');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Aanbod').should('exist');
    cy.url().should('match', aanbodPageUrlPattern);
  });

  describe('Aanbod page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(aanbodPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Aanbod page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/aanbod/new$'));
        cy.getEntityCreateUpdateHeading('Aanbod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aanbodPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/aanbods',
          body: aanbodSample,
        }).then(({ body }) => {
          aanbod = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/aanbods+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [aanbod],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(aanbodPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Aanbod page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('aanbod');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aanbodPageUrlPattern);
      });

      it('edit button click should load edit Aanbod page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Aanbod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aanbodPageUrlPattern);
      });

      it('edit button click should load edit Aanbod page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Aanbod');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aanbodPageUrlPattern);
      });

      it('last delete button click should delete instance of Aanbod', () => {
        cy.intercept('GET', '/api/aanbods/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('aanbod').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', aanbodPageUrlPattern);

        aanbod = undefined;
      });
    });
  });

  describe('new Aanbod page', () => {
    beforeEach(() => {
      cy.visit(`${aanbodPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Aanbod');
    });

    it('should create an instance of Aanbod', () => {
      cy.get(`[data-cy="naam"]`).type('yearningly cruise warm');
      cy.get(`[data-cy="naam"]`).should('have.value', 'yearningly cruise warm');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        aanbod = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', aanbodPageUrlPattern);
    });
  });
});
