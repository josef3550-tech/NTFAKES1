package com.example.data.repository

import com.example.data.local.FactCheckDao
import com.example.data.local.FactCheckEntity
import com.example.data.model.FactCheckResult
import com.example.data.model.FactCheckVerdict
import com.example.data.model.QuizQuestion
import com.example.data.model.ReliableSource
import com.example.data.remote.GeminiFactChecker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FactCheckRepository(private val dao: FactCheckDao) {

    val savedFactChecks: Flow<List<FactCheckResult>> = dao.getAllFactChecks().map { list ->
        list.map { it.toFactCheckResult() }
    }

    val bookmarkedFactChecks: Flow<List<FactCheckResult>> = dao.getBookmarkedFactChecks().map { list ->
        list.map { it.toFactCheckResult() }
    }

    suspend fun verifyClaimWithAI(
        text: String,
        userPrediction: FactCheckVerdict? = null,
        category: String = "Geral"
    ): FactCheckResult {
        val result = GeminiFactChecker.verifyClaim(text, userPrediction, category)
        dao.insertFactCheck(FactCheckEntity.fromFactCheckResult(result))
        return result
    }

    suspend fun toggleBookmark(id: String, currentStatus: Boolean) {
        dao.updateBookmarkStatus(id, !currentStatus)
    }

    suspend fun deleteCheck(id: String) {
        dao.deleteFactCheck(id)
    }

    suspend fun saveCheck(result: FactCheckResult) {
        dao.insertFactCheck(FactCheckEntity.fromFactCheckResult(result))
    }

    // Pre-loaded Trending Viral News and Fact-Checks
    fun getTrendingFactChecks(): List<FactCheckResult> {
        return listOf(
            FactCheckResult(
                id = "trend-1",
                claimTitle = "Água com limão em jejum previne ou cura o câncer?",
                originalText = "Mensagem que circula no WhatsApp afirma que tomar um copo de água morna com limão todas as manhãs altera o pH do sangue e destrói células cancerígenas.",
                verdict = FactCheckVerdict.FALSO,
                verdictExplanation = "É FALSO. O pH do sangue humano é rigorosamente regulado pelo sistema renal e respiratório entre 7,35 e 7,45 e não pode ser alterado por alimentos ou bebidas. O limão é rico em vitamina C e antioxidantes benéficos para a imunidade geral, mas não possui qualquer propriedade profilática ou terapêutica capaz de destruir tumores.",
                keyPoints = listOf(
                    "O sangue possui mecanismos tampão que mantêm o pH estável independente da dieta.",
                    "Não há nenhum estudo clínico em humanos que comprove que alimentos ácidos ou alcalinos curem o câncer.",
                    "O Instituto Nacional de Câncer (INCA) alerta que atrasar o tratamento convencional põe a vida em risco."
                ),
                sources = listOf(
                    ReliableSource("INCA (Instituto Nacional de Câncer)", "Órgão do Ministério da Saúde", "Boletim de Mitos e Verdades sobre prevenção oncológica.", "https://www.gov.br/inca"),
                    ReliableSource("Agência Lupa", "Agência de Checagem", "Análise de postagens virais sobre alcalinização do sangue.", "https://lupa.uol.com.br"),
                    ReliableSource("Sociedade Brasileira de Oncologia Clínica (SBOC)", "Sociedade Médica", "Posicionamento sobre terapias alternativas sem comprovação.", "https://sboc.org.br")
                ),
                category = "Saúde",
                tipsToSpotFake = "Cuidado com receitas caseiras que prometem substituir tratamentos complexos como quimioterapia ou cirurgia."
            ),
            FactCheckResult(
                id = "trend-2",
                claimTitle = "Satélites da internet Starlink podem ser vistos a olho nu no céu?",
                originalText = "Vídeos nas redes mostram uma linha de luzes brilhantes no céu noturno e afirmam ser uma frota de OVNIs ou evento místico.",
                verdict = FactCheckVerdict.VERDADEIRO,
                verdictExplanation = "É VERDADEIRO. Logo após o lançamento pela empresa SpaceX, os satélites da frota Starlink orbitam em baixas altitudes e refletem a luz do Sol, formando uma fileira perfeitamente alinhada de pontos brilhantes visíveis a olho nu antes de se dispersarem em suas órbitas finais.",
                keyPoints = listOf(
                    "O fenômeno ocorre frequentemente após novos lançamentos de foguetes Falcon 9.",
                    "A visão do 'comboio de luzes' dura alguns dias até que os satélites subam para a altitude operacional de 550 km.",
                    "Astrônomos de observatórios internacionais registram e rastreiam a passagem desses satélites."
                ),
                sources = listOf(
                    ReliableSource("Observatório Nacional (ON/MCTI)", "Instituto de Pesquisa Científica", "Informativo sobre fenômenos astronômicos e satélites artificiais.", "https://www.gov.br/observatorio"),
                    ReliableSource("G1 Fato ou Fake", "Jornalismo de Checagem", "Reportagem explicando os avistamentos de satélites no Brasil.", "https://g1.globo.com/fato-ou-fake/"),
                    ReliableSource("Agência Espacial Brasileira (AEB)", "Órgão Governamental Espacial", "Registros de rastreamento de tráfego orbital.", "https://www.gov.br/aeb")
                ),
                category = "Ciência & Tecnologia",
                tipsToSpotFake = "Antes de concluir que um brilho no céu é sobrenatural, consulte sites de rastreamento de satélites em tempo real como o Heavens-Above."
            ),
            FactCheckResult(
                id = "trend-3",
                claimTitle = "Comer chocolate amargo diariamente traz benefícios para a saúde cardiovascular?",
                originalText = "Postagens afirmam que chocolate com alto teor de cacau melhora a circulação e reduz o risco de doenças no coração.",
                verdict = FactCheckVerdict.VERDADEIRO,
                verdictExplanation = "É VERDADEIRO (COM MODERAÇÃO). O cacau é rico em flavonoides, compostos antioxidantes que auxiliam na vasodilatação e na melhora da pressão arterial. No entanto, o benefício é associado ao chocolate com no mínimo 70% de cacau e em pequenas porções diárias (15g a 30g), já que o excesso de açúcar e gordura traz efeitos opostos.",
                keyPoints = listOf(
                    "Flavonoides do cacau aumentam a produção de óxido nítrico no endotélio vascular.",
                    "Chocolates ao leite e brancos contêm pouco cacau e altos níveis de gordura saturada e açúcar.",
                    "Estudos da Sociedade Europeia de Cardiologia endossam o consumo moderado como parte de dieta equilibrada."
                ),
                sources = listOf(
                    ReliableSource("Sociedade Brasileira de Cardiologia (SBC)", "Sociedade Médica", "Diretrizes de nutrição e prevenção de doenças cardiovasculares.", "https://www.portal.cardiol.br"),
                    ReliableSource("Harvard T.H. Chan School of Public Health", "Instituição Acadêmica Global", "Análise de nutrientes e flavonoides do cacau.", "https://www.hsph.harvard.edu"),
                    ReliableSource("Estadão Verifica", "Agência de Checagem", "Artigo detalhando a diferença entre pesquisas com cacau puro e chocolates ultraprocessados.", "https://www.estadao.com.br/verifica")
                ),
                category = "Alimentação & Saúde",
                tipsToSpotFake = "Certifique-se de diferenciar propriedades de um ingrediente isolado (cacau) de produtos alimentícios ultraprocessados cheios de açúcar."
            ),
            FactCheckResult(
                id = "trend-4",
                claimTitle = "Aplicativos de mensagens vão passar a cobrar mensalidade por cada mensagem enviada?",
                originalText = "Corrente antiga que volta a circular alega que o aplicativo se tornará pago amanhã e pede para repassar para 10 contatos para continuar grátis.",
                verdict = FactCheckVerdict.FALSO,
                verdictExplanation = "É FALSO. Esse é um boato clássico (hoax) que circula na internet há mais de uma década. As plataformas oficiais de mensagens não cobram pelo envio de mensagens individuais para usuários comuns nem utilizam repasses de correntes para verificar quais contas estão ativas.",
                keyPoints = listOf(
                    "O modelo de negócios dessas plataformas é baseado em serviços corporativos (Business APIs) ou anúncios.",
                    "Mudar os termos de serviço exige notificação formal nas configurações do app ou lojas oficiais.",
                    "Pedir para repassar mensagens para 'ativar serviços' é um padrão clássico de spammers e golpes."
                ),
                sources = listOf(
                    ReliableSource("UOL Confere", "Checagem de Notícias", "Desmistificação da corrente recorrente de cobrança em aplicativos.", "https://noticias.uol.com.br/comprova/"),
                    ReliableSource("Aos Fatos", "Jornalismo de Verificação", "Histórico de fakes e correntes de repasse no Brasil.", "https://www.aosfatos.org"),
                    ReliableSource("CERT.br (Centro de Estudos de Incidentes de Segurança)", "Órgão de Segurança Digital", "Cartilha de segurança contra correntes e golpes cibernéticos.", "https://cartilha.cert.br")
                ),
                category = "Tecnologia & Golpes",
                tipsToSpotFake = "Qualquer mensagem que exija repasse para 'X pessoas' sob ameaça de bloqueio de conta é 100% falsa."
            )
        )
    }

    // Quiz Questions for "Desafio Fato ou Fake"
    fun getQuizQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = "q1",
                headline = "Colocar uma pilha no congelador faz com que ela recarregue completamente?",
                context = "Viral no TikTok e YouTube alega que congelar pilhas alcalinas fracas restaura sua energia química para mais alguns meses de uso.",
                correctVerdict = FactCheckVerdict.FALSO,
                detailedExplanation = "Colocar pilhas na geladeira ou congelador não recarrega a energia química. Na verdade, a umidade e a condensação do congelador podem oxidar os contatos metálicos e provocar vazamentos de fluidos corrosivos perigosos. As pilhas recarregáveis só recuperam carga quando conectadas a um carregador elétrico adequado.",
                sources = listOf(
                    ReliableSource("G1 Fato ou Fake", "Agência de Checagem", "Análise de testes de laboratório com pilhas em baixa temperatura.", "https://g1.globo.com/fato-ou-fake/"),
                    ReliableSource("Instituto de Física da USP", "Instituição Científica", "Explicação química sobre reações eletroquímicas em pilhas.", "https://www.if.usp.br")
                ),
                category = "Ciência & Tecnologia"
            ),
            QuizQuestion(
                id = "q2",
                headline = "O Brasil é o maior produtor e exportador mundial de café?",
                context = "Afirmação em fóruns sobre economia e agronegócio destacando a posição do mercado brasileiro no comércio global.",
                correctVerdict = FactCheckVerdict.VERDADEIRO,
                detailedExplanation = "O Brasil ocupa o primeiro lugar isolado na produção e exportação mundial de café há mais de 150 anos. De acordo com dados do Ministério da Agricultura e da Organização Internacional do Café (ICO), o país responde por cerca de um terço de todo o café consumido no planeta.",
                sources = listOf(
                    ReliableSource("IBGE (Instituto Brasileiro de Geografia e Estatística)", "Órgão Oficial de Estatística", "Levantamento Sistemático da Produção Agrícola.", "https://www.ibge.gov.br"),
                    ReliableSource("OIC (Organização Internacional do Café)", "Entidade Internacional", "Relatório de comércio global de commodities cafeeiras.", "https://www.ico.org")
                ),
                category = "Economia & Geografia"
            ),
            QuizQuestion(
                id = "q3",
                headline = "Usar celular enquanto o aparelho está carregando na tomada atrai raios em tempestades?",
                context = "Mensagens de texto afirmam que a fiação elétrica funciona como uma antena atratora de descargas atmosféricas para quem segura o telefone.",
                correctVerdict = FactCheckVerdict.FALSO,
                detailedExplanation = "O celular e o carregador não atraem raios. No entanto, se um raio atingir diretamente a rede elétrica de uma residência sem proteção adequada, pode haver uma sobretensão que danifica aparelhos conectados à tomada. Por segurança contra choques elétricos caso ocorra uma grande oscilação de tensão na rede, recomenda-se evitar tocar em aparelhos ligados à rede elétrica durante tempestades severas, mas não porque o celular 'atrai' o raio.",
                sources = listOf(
                    ReliableSource("ELAT / INPE (Grupo de Eletricidade Atmosférica)", "Instituto Científico de Pesquisas Espaciais", "Cartilha de proteção contra raios e mitos elétricos.", "https://www.inpe.br/elat"),
                    ReliableSource("Agência Lupa", "Agência de Checagem", "Verificação sobre o mito de atração de raios por celulares.", "https://lupa.uol.com.br")
                ),
                category = "Ciência & Segurança"
            ),
            QuizQuestion(
                id = "q4",
                headline = "A Floresta Amazônica é o 'pulmão do mundo' e produz a maior parte do oxigênio do planeta?",
                context = "Publicação ambiental frequente afirmando que se a Amazônia desaparecer, o oxigênio atmosférico acabará em poucas semanas.",
                correctVerdict = FactCheckVerdict.PARCIALMENTE_VERDADEIRO,
                detailedExplanation = "É uma simplificação popular mas cientificamente imprecisa. A Floresta Amazônica produz um volume imenso de oxigênio pela fotossíntese, porém consome quase toda essa quantidade em sua própria respiração vegetal e decomposição orgânica. O verdadeiro 'pulmão do mundo' em produção líquida de oxigênio são os plânctons (fitoplâncton) nos oceanos. O valor inestimável da Amazônia reside na regulação do clima global, ciclo de chuvas e biodiversidade.",
                sources = listOf(
                    ReliableSource("INPA (Instituto Nacional de Pesquisas da Amazônia)", "Centro de Pesquisas Científicas", "Estudos do ciclo de carbono e oxigênio na bacia amazônica.", "https://www.gov.br/inpa"),
                    ReliableSource("Projeto Comprova", "Consórcio de Checagem", "Explicação sobre fotossíntese florestal e fitoplânctons oceânicos.", "https://projetocomprova.com.br")
                ),
                category = "Meio Ambiente"
            )
        )
    }
}
